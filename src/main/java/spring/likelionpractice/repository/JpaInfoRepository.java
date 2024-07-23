package spring.likelionpractice.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import spring.likelionpractice.domain.Info;
import spring.likelionpractice.domain.Member;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaInfoRepository implements InfoRepository {

    private final EntityManager em;

    @Override
    public Info save(Info info) {
        em.persist(info);
        return info;
    }

    // 사실상 primary key id는 의미가 없고 Member 테이블에 대응되는 member_id 값이랑 대응이 되야 함
    @Override
    public Info findByMemberId(Member memberId) {
        return em.find(Info.class, memberId);
    }

    @Override
    public List<Info> finaAll() {
        return em.createQuery("Select i from Info i", Info.class).getResultList();
    }

    @Override
    public Info findById(Long infoId) {
        return em.find(Info.class, infoId);
    }
}
