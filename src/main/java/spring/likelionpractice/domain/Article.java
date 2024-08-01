package spring.likelionpractice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.web.multipart.MultipartFile;
import spring.likelionpractice.service.ImageUtility;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Article {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member writer;

    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    @Setter
    private String title;
    @Setter
    @Column(columnDefinition = "TEXT")
    private String content;
    @Setter
    private Long likeCount = 0L;
    @Setter
    @Column(name = "image", columnDefinition = "MEDIUMBLOB")
    private byte[] articleImage;

//    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<ArticleImage> articleImages = new ArrayList<>();       // 게시물 이미지 업로드 리스트 추가

    public Article(String title, String content, Member writer, byte[] articleImage) {
        this.createDate = LocalDateTime.now();
        this.updateDate = this.createDate;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.likeCount = 0L;
        this.articleImage = articleImage;
    }

    public void update(String title, String content, byte[] articleImage) {
        this.title = title;
        this.content = content;
        this.updateDate = LocalDateTime.now();
        this.articleImage = articleImage;
    }

    public String arrayToImage() {
        if(this.articleImage == null) {
            return null;
        }
        return ImageUtility.encodeImage(this.articleImage);
    }
}
