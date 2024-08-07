package spring.likelionpractice.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.likelionpractice.domain.Article;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ArticleDTO {

    @Data
    public static class ResponseArticle {
        @Schema(description = "id", example = "게시물 아이디")
        private Long id;
        @Schema(description = "제목", example = "게시물 제목입니다.")
        private String title;
        @Schema(description = "내용", example = "게시물 내용입니다.")
        private String content;
        @Schema(description = "작성자", example = "qwer1234")
        private String writer;
        @Schema(description = "작성시간", example = "2024-04-04")
        private LocalDateTime createDate;
        @Schema(description = "작성자가 수정을 하였는지 여부", example = "true, false")
        private boolean isChange;
        @Schema(description = "게시물 좋아요 수", example = "3")
        private Long likeCount;
        @Schema(description = "게시물 이미지", example = "AISKCJWE42")
        private String image;

        public ResponseArticle(Article article) {
            this.id = article.getId();
            this.title = article.getTitle();
            this.content = article.getContent();
            this.writer = article.getWriter().getName();
            this.createDate = article.getCreateDate();
            this.likeCount = article.getLikeCount();
            this.image = article.arrayToImage();

            if(article.getCreateDate().equals(article.getUpdateDate())) {
                this.isChange = false;
            } else {
                this.isChange = true;
            }
        }
    }

    @Data
    public static class requestArticle {
        @Schema(description = "제목", example = "게시물 제목입니다.")
        private String title;
        @Schema(description = "내용", example = "게시물 내용입니다.")
        private String content;
        @Schema(description = "이미지", example = "게시물 이미지입니다.")
        private MultipartFile image;
    }

    @Data
    public static class deleteArticle {
        @Schema(description = "로그인시 토큰", example = "HYKCJMINCWJ42")
        private String token;
    }

    @Data
    public static class ResponseArticleList {
        @Schema(description = "아이디", example = "1")
        private Long id;
        @Schema(description = "제목", example = "게시물 제목입니다.")
        private String title;
        @Schema(description = "내용", example = "게시물 내용입니다.")
        private String content;
        @Schema(description = "작성자", example = "qwer1234")
        private String writer;
        @Schema(description = "작성시간", example = "2024-04-04")
        private LocalDateTime createDate;
        @Schema(description = "게시물 좋아요 수", example = "3")
        private Long likeCount;

        public ResponseArticleList(Article article) {
            this.id = article.getId();
            this.title = article.getTitle();
            this.content = article.getContent();
            this.writer = article.getWriter().getName();
            this.createDate = article.getCreateDate();
            this.likeCount = article.getLikeCount();
        }
    }

    @Data
    public static class RequestImgArticle {
        @Schema(description = "제목", example = "게시물 제목입니다.")
        private String title;
        @Schema(description = "내용", example = "게시물 내용입니다.")
        private String content;
        @Schema(description = "이미지", example = "이미지입니다.")
        private MultipartFile image;
    }
}
