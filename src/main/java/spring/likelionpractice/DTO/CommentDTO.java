package spring.likelionpractice.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import spring.likelionpractice.domain.Comment;

import java.time.LocalDateTime;

public class CommentDTO {
    @Data
    public static class CommentCreateRequest {
        @Schema(description = "게시물 Id(primary key)", example = "1")
        private Long articleId;
        @Schema(description = "댓글 내용", example = "댓글입니다.")
        private String content;
    }

    @Data
    public static class CommentUpdateRequest {
        @Schema(description = "댓글 내용", example = "댓글입니다.")
        private String content;
        @Schema(description = "댓글 Id(primary key)", example = "1")
        private Long commentId;
    }

    @Data
    public static class CommentDeleteRequest {
        @Schema(description = "댓글 Id(primary key)", example = "1")
        private Long commentId;
    }

    @Data
    public static class CommentResponse {
        @Schema(description = "댓글 아이디", example = "1")
        private Long id;
        @Schema(description = "댓글 내용", example = "댓글 내용입니다.")
        private String content;
        @Schema(description = "댓글 생성 시간", example = "2024-04-22")
        private LocalDateTime createDate;
        @Schema(description = "댓글 수정 여부", example = "true, false")
        private boolean isUpdate;
        @Schema(description = "댓글 작성자 이름", example = "홍길동")
        private String writer;
        @Schema(description = "댓글 작성자 아이디", example = "qwer1234")
        private String writer_id;

        public CommentResponse(Comment comment) {
            id = comment.getId();
            content = comment.getContent();
            createDate = comment.getCreatedDate();
            isUpdate = !comment.getCreatedDate().equals(comment.getUpdatedDate());
            writer = comment.getWriter().getName();
            writer_id = comment.getWriter().getUserId();
        }
    }

    @Data
    public static class CommentCountResponse {
        private Long commentCount;

        public CommentCountResponse(Comment comment) {

        }
    }
}
