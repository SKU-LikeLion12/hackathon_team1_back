package spring.likelionpractice.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import spring.likelionpractice.domain.Article;
import spring.likelionpractice.domain.Like;
import spring.likelionpractice.domain.Member;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JpaLikeRepository implements LikeRepository {

    private final EntityManager em;

    @Override
    public void clickLike(Like like) {      // 유저당 게시물에 좋아요 + 1
        em.persist(like);
    }

    @Override
    public void deleteLike(Like like) {      // 한번 더 누르면 좋아요 취소
        em.remove(like);
    }

    @Override
    public Like findByMemberAndArticle(Member member, Article article) {        // 주어진 멤버와 게시글에 대한 좋아요가 있는지 확인
        return em.createQuery("SELECT l FROM Like l WHERE l.member = :member AND l.article = :article", Like.class)
                .setParameter("member", member)
                .setParameter("article", article)
                .getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public List<Article> findByArticleLikeMe(Member member) {       // 내가 좋아요한 게시물 목록 조회
        return em.createQuery("Select Distinct l.article from Like l where l.member = :member", Article.class)
                .setParameter("member", member).getResultList();
    }

    @Override
    public List<Article> findByArticleLikeCount() {      // 게시글 리스트 불러올 때 각 게시글별 좋아요 개수 불러오기
        return em.createQuery("Select a from Article a Left Join Like l on a.id = l.article.id " +
                "group by a.id Order by Count(l) Desc", Article.class).getResultList();
    }

    @Override
    public List<Article> findBySortLike() {         // 게시글 좋아요 개수 내림차순 정렬 조회 (좋아요 많은 거 부터)
        return em.createQuery("Select a from Article a Order by a.likeCount Desc", Article.class)
                .getResultList();
    }
}
