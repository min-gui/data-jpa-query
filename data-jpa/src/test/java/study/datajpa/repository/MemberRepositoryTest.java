package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.domain.Member;
import study.datajpa.domain.Team;
import study.datajpa.dto.MemberDto;

import java.util.List;

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





        
    

}