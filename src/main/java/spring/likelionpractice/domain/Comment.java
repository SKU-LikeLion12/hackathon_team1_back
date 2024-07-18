package spring.likelionpractice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class Comment {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private Member writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;
    private String content;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    public Comment(Member writer, Article article, String content) {
        this.writer = writer;
        this.article = article;
        this.content = content;
        this.createdDate = this.updatedDate = LocalDateTime.now();
    }

    public void updateComment(String content) {
        this.content = content;
        this.updatedDate = LocalDateTime.now();
    }
}
