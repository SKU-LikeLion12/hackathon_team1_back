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
                                                                // LONGBLOB 4GB
    @Lob                                                       // MEDIUMBLOB 8MB
    @Column(nullable = false, columnDefinition = "MEDIUMBLOB") // TINYBLOB  256byte
    private byte[] image;                                      // BLOB      64kb

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    public ArticleImage(byte[] image, Article article) {
        this.image = image;
        this.article = article;
    }

    public void update(byte[] image) {
        this.image = image;
    }
}
