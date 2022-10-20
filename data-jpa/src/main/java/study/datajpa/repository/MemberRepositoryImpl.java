package study.datajpa.repository;

import lombok.RequiredArgsConstructor;
import study.datajpa.domain.Member;

import javax.persistence.EntityManager;
import java.util.List;


@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{
//클래스 이름 맞춰줘야 된다.

    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m")
                .getResultList();
    }
}
