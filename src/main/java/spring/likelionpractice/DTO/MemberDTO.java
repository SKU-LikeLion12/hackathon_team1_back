package spring.likelionpractice.DTO;

import lombok.*;
import org.springframework.web.bind.annotation.*;
import spring.likelionpractice.domain.Member;

@RestController
@RequiredArgsConstructor
public class MemberDTO {

    @Getter
    @Setter
    @Data
    public class UserInfoRequest {
        private String name;
        private String phone;
    }

    @Getter
    @Setter
    @Data
    public class VerificationRequest {
        private String phone;
        private String verificationCode;
    }

    @Data
    public static class MemberCreateRequest {
        private String phone;
        private String userId;
        private String password;
        private String name;
    }

    @Data
    public static class MemberLoginRequest {
        private String userId;
        private String password;
    }

    @AllArgsConstructor
    @Data
    public static class MemberResponse {        // 내부 클래스를 static으로 선언하여 외부 클래스 new 키워드 없이 생성 가능
        private String userId;
        private String nickname;
    }

    @Data
    public static class MemberUpdateRequest {
        private String token;
        private String nickname;
    }

    @Data
    public static class MemberDeleteRequest {
        private String token;
    }
}
