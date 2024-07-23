package spring.likelionpractice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class ArticleImage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String url;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    public ArticleImage(String url, Article article) {
        this.url = url;
        this.article = article;
    }

    public void update(String url) {
        this.url = url;
    }
}
