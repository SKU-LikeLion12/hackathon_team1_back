package spring.likelionpractice.DTO;

import lombok.Data;
import spring.likelionpractice.domain.Article;

public class LikeDTO {
    @Data
    public static class LikeRequest {
        private Long articleId;
        private String token;
    }

    @Data
    public static class LikeCountResponse {     // 게시물 좋아요 개수 불러오기
        private Long likeCount;
        private String title;

        public LikeCountResponse(Article article) {
            this.likeCount = article.getLikeCount();
            this.title = article.getTitle();
        }
    }

    @Data
    public static class LikeResponse {
        private boolean liked;
        private Long likeCount;

        public LikeResponse(boolean liked, Long likeCount) {
            this.liked = liked;
            this.likeCount = likeCount;
        }
    }
}
