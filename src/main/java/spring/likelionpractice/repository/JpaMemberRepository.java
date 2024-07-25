package spring.likelionpractice.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import spring.likelionpractice.domain.Member;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaMemberRepository implements MemberRepository {

    private final EntityManager em;

    @Override
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    @Override
    public Member findById(Long id) {
        return em.find(Member.class, id);
    }

    @Override
    public Member findByUserId(String userId) {
        try {
            return em.createQuery("Select m from Member m where m.userId = :userId", Member.class)
                    .setParameter("userId", userId).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Optional<Member> findByPhone(String phone) {
        try {
            Member member = em.createQuery("SELECT m FROM Member m WHERE m.phone = :phone", Member.class)
                    .setParameter("phone", phone)
                    .getSingleResult();
            return Optional.of(member);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Member> findAll() {
        return em.createQuery("Select m from Member m", Member.class).getResultList();
    }

    @Override
    public void deleteMember(Member member) {
        em.remove(member);
    }

    @Override
    public List<Member> findByName(String name) {
        return em.createQuery("Select m from Member m where m.nickname = :name", Member.class)
                .setParameter("name", name).getResultList();
    }

}
