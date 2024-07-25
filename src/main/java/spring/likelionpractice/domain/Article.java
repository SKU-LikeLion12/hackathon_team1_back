package spring.likelionpractice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Article {
    @Id @GeneratedValue
    private Long id; // pk

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member writer; // 게시글 작성자

    private LocalDateTime createDate; //생성된 시간
    private LocalDateTime updateDate; //수정된 시간

    private String title; // 게시글 제목
    @Column(columnDefinition = "TEXT")
    private String content; // 게시글 내용
    @Setter
    private Long likeCount = 0L; // 게시글 좋아요 수

    public Article(String title, String content, Member writer) {
        this.createDate = LocalDateTime.now();
        this.updateDate = this.createDate;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.likeCount = 0L;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
        this.updateDate = LocalDateTime.now();
    }

}
