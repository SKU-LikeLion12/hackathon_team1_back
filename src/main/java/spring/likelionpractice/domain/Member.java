package spring.likelionpractice.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Member {
    @Id @GeneratedValue
    private Long id; // pk
    @Column(unique = true)
    private String userId; // 아이디
    private String password;// 비밀번호

    @Setter
    private String nickname;

    @Setter
    @Getter
    @Column(unique = true)
    private String phone; // 전화번호

    @Setter
    @Getter
    private String fileName; // 회원 이미지

    public Member(String userId, String password, String phone, String fileName) {
        this.userId = userId;
        this.setPassword(password);
        this.setPhone(phone);
        this.setFileName(fileName);
    }

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void setPassword(String password) {
        this.password = passwordEncoder.encode(password);
    }

    public boolean checkPassword(String password) {
        return passwordEncoder.matches(password, this.password);
    }
}
