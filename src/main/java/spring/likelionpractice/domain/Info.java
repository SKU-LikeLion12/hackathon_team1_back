package spring.likelionpractice.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Info { // 마이페이지

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //pk

    private LocalDateTime nosmk; // 금연시작일시
    private LocalDateTime startsmk; // 흡연시작일시
    private int amountsmk; // 하루 흡연량
    private int price; // 담배 가격
    private int ciga; // 담배 한갑당 개비 수


    public Info(LocalDateTime nosmk, LocalDateTime startsmk, int amountsmk, int ciga) {
        this.nosmk = nosmk;
        this.startsmk = startsmk;
        this.amountsmk = amountsmk;
        this.ciga = ciga;
    }

}
