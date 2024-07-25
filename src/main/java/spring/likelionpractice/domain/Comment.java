package spring.likelionpractice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // pk

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private Member writer; // 작성자 아이디

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article; // 게시물 아이디

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parentComment; // 부모 댓글 *** 대댓글 다시 한 번 맞추기

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> childComments = new ArrayList<>(); // 자식 댓글 목록

    private String content; // 댓글 내용
    private LocalDateTime createdDate; // 댓글 생성 시간
    private LocalDateTime updatedDate; // 댓글 수정 시간

    public Comment(Member writer, Article article, String content, Comment parentcomment) {
        this.writer = writer;
        this.article = article;
        this.content = content;
        this.createdDate = this.updatedDate = LocalDateTime.now();
        this.parentComment = parentcomment;
    }

    public void updateComment(String content) { // 댓글 수정 메소드
        this.content = content;
        this.updatedDate = LocalDateTime.now();
    }

    public void addChildComment(Comment childComment){ // 대댓글 추가 메소드
        childComments.add(childComment);
        childComment.setParentComment(this);
    }
}
