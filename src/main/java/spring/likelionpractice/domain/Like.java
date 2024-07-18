package spring.likelionpractice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "likes")
@NoArgsConstructor
public class Like {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne @JoinColumn(name = "article_id")
    private Article article;

    public Like(Member member, Article article) {
        this.member = member;
        this.article = article;
    }
}
