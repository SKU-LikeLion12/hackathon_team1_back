package spring.likelionpractice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Entity
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String userId;
    private String password;

    @Setter
    @Column(unique = true)
    private String email;
    @Setter
    private String name;

    @Setter @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate noSmk;        // 금연 시작 일시

    @Setter @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startSmk;     // 흡연 시작 일시
    @Setter
    private int amountSmk;          // 하루 흡연량
    @Setter
    private int price;              // 담배 가격
    @Setter
    private int ciga;               // 담배 한갑당 개비수
    @Setter
    private int tar;                // 타르 양

    @Setter @Column(name = "image", columnDefinition = "MEDIUMBLOB")        // 프로필 이미지
    private byte[] image = null;

    public Member(String userId, String password, String name, String email,
                    LocalDate noSmk, LocalDate startSmk, int amountSmk, int price,
                    int ciga, int tar) {
        this.userId = userId;
        this.setPassword(password);
        this.name = name;
        this.email = email;
        this.noSmk = noSmk;
        this.startSmk = startSmk;
        this.amountSmk = amountSmk;
        this.price = price;
        this.ciga = ciga;
        this.tar = tar;
    }

    public Member(byte[] image) {
        this.image = image;
    }

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void setPassword(String password) {
        this.password = passwordEncoder.encode(password);
    }

    public boolean checkPassword(String password) {
        return passwordEncoder.matches(password, this.password);
    }

}
