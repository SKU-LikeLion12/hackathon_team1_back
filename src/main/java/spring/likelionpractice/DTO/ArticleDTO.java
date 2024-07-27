package spring.likelionpractice.DTO;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import spring.likelionpractice.domain.Article;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class ArticleDTO {

    @Data
    public static class ResponseArticle {
        private String title;
        private String content;
        private String writer;
        private LocalDateTime createDate;
        private boolean isChange;
        private Long likeCount;
        private String image;

        public ResponseArticle(Article article) {
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
        private String title;
        private String content;
        private String token;
        private String image;
    }

    @Data
    public static class deleteArticle {
        private String token;
    }

}
