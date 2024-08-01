package spring.likelionpractice.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import spring.likelionpractice.domain.Article;

public class LikeDTO {
    @Data
    public static class LikeRequest {
        @Schema(description = "게시물 id(primary key)", example = "1")
        private Long articleId;
    }

    @Data
    public static class LikeCountResponse {     // 게시물 좋아요 개수 불러오기
        @Schema(description = "게시물 좋아요 개수", example = "4")
        private Long likeCount;
        @Schema(description = "게시물 제목", example = "게시물 제목 입니다.")
        private String title;

        public LikeCountResponse(Article article) {
            this.likeCount = article.getLikeCount();
            this.title = article.getTitle();
        }
    }

    @Data
    public static class LikeResponse {
        @Schema(description = "좋아요를 눌렀는지 안눌렀는지", example = "true, false")
        private boolean liked;
        @Schema(description = "좋아요 개수", example = "4")
        private Long likeCount;

        public LikeResponse(boolean liked, Long likeCount) {
            this.liked = liked;
            this.likeCount = likeCount;
        }
    }

    @Data
    public static class CountResponse {
        @Schema(description = "좋아요 개수", example = "3")
        private Long likeCount;

        public CountResponse(Long likeCount) {
            this.likeCount = likeCount;
        }
    }
}
