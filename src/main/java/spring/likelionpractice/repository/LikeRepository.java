package spring.likelionpractice.repository;

import spring.likelionpractice.domain.Article;
import spring.likelionpractice.domain.Like;
import spring.likelionpractice.domain.Member;

import java.util.List;

public interface LikeRepository {
    public void clickLike(Like like);       // 유저당 게시글에 좋아요 + 1

    public void deleteLike(Like like);      // 한번 더 누르면 좋아요 취소

    public Like findByMemberAndArticle(Member member, Article article);     // 주어진 멤버와 게시글에 대한 좋아요가 있는지 확인

    public List<Article> findByArticleLikeMe(Member member);  // 내가 좋아요한 게시물 목록 조회

    public List<Article> findByArticleLikeCount();        // 게시글 리스트 불러올 때 각 게시글별 좋아요 개수 불러오기

    public List<Article> findBySortLike();          // 게시글 좋아요 개수 내림차순 정렬 조회(좋아요 많은 거 부터)

}
