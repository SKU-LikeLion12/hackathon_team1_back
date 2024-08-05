package spring.likelionpractice.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
public class MemberDTO {

    @Data
    public static class MemberCreateRequest {
        @Schema(description = "이름", example = "홍길동")
        private String name;
        @Schema(description = "아이디", example = "qwer1234")
        private String userId;
        @Schema(description = "패스워드", example = "asdf1234")
        private String password;
        @Schema(description = "이메일", example = "qwer1234@naver.com")
        private String email;
        @Schema(description = "금연시작날짜", example = "2023-02-22")
        private LocalDate noSmk;
        @Schema(description = "흡연시작날짜", example = "2021-01-13")
        private LocalDate startSmk;
        @Schema(description = "하루 흡연량", example = "20")
        private int amountSmk;
        @Schema(description = "담배 가격", example = "4500")
        private int price;
        @Schema(description = "한갑에 있는 담배양", example = "20")
        private int ciga;
        @Schema(description = "타르 양", example = "4")
        private int tar;
    }

    @Data
    public static class MemberLoginRequest {
        @Schema(description = "아이디", example = "qwer1234")
        private String userId;
        @Schema(description = "비밀번호", example = "asdf1234")
        private String password;
    }

    @Data
    public static class MemberUpdateRequest {
        @Schema(description = "이름", example = "홍길동")
        private String name;
        @Schema(description = "이메일", example = "qwer@naver.com")
        private String email;
        @Schema(description = "금연시작날짜", example = "2023-02-22")
        private LocalDate noSmk;
        @Schema(description = "흡연시작날짜", example = "2021-01-13")
        private LocalDate startSmk;
        @Schema(description = "하루 흡연량", example = "20")
        private int amountSmk;
        @Schema(description = "담배 가격", example = "4500")
        private int price;
        @Schema(description = "한갑에 있는 담배양", example = "20")
        private int ciga;
        @Schema(description = "타르 양", example = "4")
        private int tar;
    }

    @AllArgsConstructor
    @Data
    public static class MemberUpdateResponse {
        @Schema(description = "이름", example = "홍길동")
        private String name;
        @Schema(description = "이메일", example = "qwer@naver.com")
        private String email;
        @Schema(description = "금연시작날짜", example = "2023-02-22")
        private LocalDate noSmk;
        @Schema(description = "흡연시작날짜", example = "2021-01-13")
        private LocalDate startSmk;
        @Schema(description = "하루 흡연량", example = "20")
        private int amountSmk;
        @Schema(description = "담배 가격", example = "4500")
        private int price;
        @Schema(description = "한갑에 있는 담배양", example = "20")
        private int ciga;
        @Schema(description = "타르 양", example = "4")
        private int tar;
        @Schema(description = "프로필 이미지", example = "ASUADS43423")
        private String image;
    }

    @Data
    public static class MemberDeleteRequest {
        @Schema(description = "토큰", example = "AUNKEWOCJSK53432")
        private String token;
    }

    @Data
    @AllArgsConstructor
    public static class LoginResponse {
        @Schema(description = "토큰", example = "AUNKEWOCJSK53432")
        private String token;
    }

    @Data
    public static class EmailRequest {
        @Schema(description = "이메일 주소", example = "qwer@naver.com")
        private String email;
    }

    @Data
    public static class IdFindRequest {
        @Schema(description = "이름", example = "홍길동")
        private String name;
        @Schema(description = "이메일 주소", example = "qwer@naver.com")
        private String email;
    }

    @Data
    public static class PasswordFindRequest {
        @Schema(description = "아이디", example = "qwer1234")
        private String userId;
        @Schema(description = "이메일", example = "qwer@naver.com")
        private String email;
    }
}
