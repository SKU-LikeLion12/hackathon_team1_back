package spring.likelionpractice.DTO;

import lombok.Data;
import spring.likelionpractice.domain.Comment;

import java.time.LocalDateTime;

public class CommentDTO {
    @Data
    public static class CommentCreateRequest {
        private Long articleId;
        private String content;
        private String token;
    }

    @Data
    public static class CommentUpdateRequest {
        private String content;
        private Long commentId;
        private String token;
    }

    @Data
    public static class CommentDeleteRequest {
        private String token;
        private Long commentId;
    }

    @Data
    public static class CommentResponse {
        private String content;
        private LocalDateTime createDate;
        private boolean isUpdate;
        private String writer;
        private String writer_id;

        public CommentResponse(Comment comment) {
            content = comment.getContent();
            createDate = comment.getCreatedDate();
            isUpdate = !comment.getCreatedDate().equals(comment.getUpdatedDate());
            writer = comment.getWriter().getNickname();
            writer_id = comment.getWriter().getUserId();
        }
    }
}
