package spring.likelionpractice.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import spring.likelionpractice.domain.Info;

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

    @Override
    public Info findById(Long id) {
        return em.find(Info.class, id);
    }

    @Override
    public List<Info> finaAll() {
        return em.createQuery("Select i from Info i", Info.class).getResultList();
    }
}
