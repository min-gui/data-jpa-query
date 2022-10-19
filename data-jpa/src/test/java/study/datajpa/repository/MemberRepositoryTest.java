package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.domain.Member;
import study.datajpa.domain.Team;
import study.datajpa.dto.MemberDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {
    
    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;
    
    @Test
    public void testMember() throws Exception {
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);
        //or else 값이 없으면 해줘야 됨.
        Member findMember = memberRepository.findById(savedMember.getId()).get();
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
     
     }

     @Test
     public void basicCRUD() throws Exception {
         //given
         Member member1 = new Member("member1");
         Member member2 = new Member("member2");
         memberRepository.save(member1);
         memberRepository.save(member2);

         //when
         Member findMember1 = memberRepository.findById(member1.getId()).get();
         Member findMember2 = memberRepository.findById(member2.getId()).get();
         assertThat(findMember1).isEqualTo(member1);
         assertThat(findMember2).isEqualTo(member2);


         //리스트 조회 검증
         List<Member> all = memberRepository.findAll();
         assertThat(all.size()).isEqualTo(2);
         
         //카운트 검증
         long count = memberRepository.count();
         assertThat(count).isEqualTo(2);
         
         //삭제 검증
         memberRepository.delete(member1);
         memberRepository.delete(member2);

         long deleteCount = memberRepository.count();
         assertThat(deleteCount).isEqualTo(0);


      }

    @Test
    public void findByUsernameAndAgeGreaterThan() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);
        List<Member> result =
                 memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);
        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void findHelloBy() throws Exception {
        //given
        List<Member> helloBy = memberRepository.findTop3HelloBy();
        //when

        //then

     }

     @Test
     public void testQuery() throws Exception {
         //given
         Member m1 = new Member("AAA", 10);
         Member m2 = new Member("BBB", 10);
         memberRepository.save(m1);
         memberRepository.save(m2);
         //when

         List<Member> result = memberRepository.findUser("AAA", 10);

         //then
         assertThat(result.get(0)).isEqualTo(m1);

      }
     @Test
     public void findUsernameList() throws Exception {
         //given
         Member m1 = new Member("AAA", 10);
         Member m2 = new Member("BBB", 10);
         memberRepository.save(m1);
         memberRepository.save(m2);
         //when
         List<String> usernameList = memberRepository.findUsernameList();
         for (String s : usernameList) {
             System.out.println("s = " + s);
             
         }
     }

    @Test
    public void findMemberDto() throws Exception {
        //given
        Team teamA = new Team("teamA");
        teamRepository.save(teamA);

        Member m1 = new Member("AAA", 10);
        m1.setTeam(teamA);
        memberRepository.save(m1);


        //when
        List<MemberDto> usernameList = memberRepository.findMemberDto();
        for (MemberDto s : usernameList) {
            System.out.println("s = " + s);

        }
    }

    @Test
    public void findByNames() throws Exception {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 10);


        //when
        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }
    
    @Test
    public void returnType() throws Exception {
        //given
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 10);
        memberRepository.save(m1);
        memberRepository.save(m2);

        //when
        //List<Member> findMember = memberRepository.findListByUsername("AAA");
        Member memberByUsername = memberRepository.findMemberByUsername("23AAA");
        System.out.println("memberByUsername = " + memberByUsername);
        //System.out.println("findMember = " + findMember);

        //결과가 2개 이상이면 예외 터진다.
        Optional<Member> findMember = memberRepository.findOptionalByUsername("dfdf");
        //System.out.println("findMember.orElse() = " + findMember.orElse());

    }

    @Test
    public void paging() throws Exception {
        //given
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));
        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.Direction.ASC, "username");

        //when ( 토털 카운트 까지 같이 날린다.)
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        //then
        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements();

        for (Member member: content){
            System.out.println("member = " + member);
        }
        System.out.println("totalElements = " + totalElements);

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }

}