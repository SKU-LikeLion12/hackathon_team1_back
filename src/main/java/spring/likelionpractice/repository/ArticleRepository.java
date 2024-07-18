package spring.likelionpractice.repository;

import spring.likelionpractice.domain.Article;

import java.util.List;

public interface ArticleRepository {
    public Article saveNewArticle(Article article);         // 게시물 저장

    public void deleteArticle(Article article);         // 게시물 삭제

    public Article findById(Long articleId);            // 게시물 찾기

    public List<Article> findAll();                 // 모든 게시물 찾기

    public List<Article> findUserAll(Long memberId);        // 멤버 아이디로 작성글 조회

    public void setLikeCount(Long articleId);       // 게시물 좋아요 카운트 + 1
}
