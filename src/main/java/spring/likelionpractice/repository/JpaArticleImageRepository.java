package spring.likelionpractice.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import spring.likelionpractice.domain.ArticleImage;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaArticleImageRepository implements ArticleImageRepository {

    private final EntityManager em;
    private final ArticleRepository articleRepository;

    @Override
    public ArticleImage save(ArticleImage articleImage) {       // 게시물 이미지 경로 저장
        em.persist(articleImage);
        return articleImage;
    }

    @Override
    public void deleteArticleImage(ArticleImage articleImage) {     // 게시물 이미지 경로 삭제
        em.remove(articleImage);
    }

    @Override
    public ArticleImage getArticleImage(Long articleImageId) {      //  특정 게시물 이미지 경로 찾기
        return em.find(ArticleImage.class, articleImageId);
    }

    @Override
    public List<ArticleImage> getAllArticleImages() {               // 게시물 이미지 모든 경로 찾기
        return em.createQuery("Select ai from ArticleImage ai", ArticleImage.class)
                .getResultList();
    }

}
