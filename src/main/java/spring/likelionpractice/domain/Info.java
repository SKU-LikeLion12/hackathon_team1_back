package spring.likelionpractice.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@NoArgsConstructor
@Getter
public class Info {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;          // 멤버와 연관 키

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate nosmk;        // 금연 시작 일시

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startsmk;     // 흡연 시작 일시
    private int amountsmk;              // 하루 흡연량
    private int price;                  // 담배 가격
    private int ciga;                   // 담배 한갑당 개비수
    private int tar;                    // 타르 양

    public Info(Member member,int amountsmk, int price, int ciga, LocalDate startsmk, LocalDate nosmk, int tar) {
        this.member = member;
        this.amountsmk = amountsmk;
        this.price = price;
        this.ciga = ciga;
        this.startsmk = startsmk;
        this.nosmk = nosmk;
        this.tar = tar;
    }
}
