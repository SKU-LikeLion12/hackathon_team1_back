package spring.likelionpractice.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import spring.likelionpractice.domain.Article;
import spring.likelionpractice.domain.Comment;
import spring.likelionpractice.domain.Member;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class JpaCommentRepository implements CommentRepository {

    private final EntityManager em;

    @Override
    public Comment findById(Long id) {
        return em.find(Comment.class, id);
    }

    @Override
    public void saveComment(Comment comment) {
        em.persist(comment);
    }

    @Override
    public void deleteComment(Comment comment) {
        em.remove(comment);
    }

    @Override
    public List<Comment> findArticleComment(Article article) {      // 게시물에 달린 댓글 목록 조회
        return em.createQuery("Select c from Comment c where c.article = :article", Comment.class)
                .setParameter("article", article).getResultList();
    }

    @Override
    public List<Comment> findMemberComment(Member member) {         // 특정 멤버가 작성한 댓글 목록 조회
        return em.createQuery("Select c from Comment c where c.writer = :member", Comment.class)
                .setParameter("member", member).getResultList();
    }

    @Override
    public List<Article> findMemberCommentArticle(Member member) {      // 특정 멤버가 댓글을 작성한 적이 있는 게시물 목록 조회
        return em.createQuery("Select distinct c.article from Comment c where c.writer = :member ", Article.class)
                .setParameter("member", member).getResultList();
    }
}
