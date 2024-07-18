package spring.likelionpractice.repository;

import spring.likelionpractice.domain.Article;
import spring.likelionpractice.domain.Comment;
import spring.likelionpractice.domain.Member;

import java.util.List;

public interface CommentRepository {
    Comment findById(Long id);              // 검색

    void saveComment(Comment comment);      // 저장

    void deleteComment(Comment comment);        // 삭제

    List<Comment> findArticleComment(Article article);      // 게시글에 달린 댓글 목록 조회

    List<Comment> findMemberComment(Member member);         // 특정 멤버가 작성한 댓글 목록 조회

    List<Article> findMemberCommentArticle(Member member);      // 특정 멤버가 댓글을 작성한 적이 있는 게시글 목록 조회

}
