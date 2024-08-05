package spring.likelionpractice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.OnError;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.hibernate.resource.beans.internal.BeansMessageLogger_$logger;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.likelionpractice.DTO.MemberDTO.*;
import spring.likelionpractice.Exception.InvalidCredentialsException;
import spring.likelionpractice.Exception.MailSendException;
import spring.likelionpractice.domain.Member;
import spring.likelionpractice.repository.MemberRepository;
import spring.likelionpractice.service.ImageUtility;
import spring.likelionpractice.service.MailService;
import spring.likelionpractice.service.MemberService;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MailService mailService;
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private int number;

    // 프론트에서 form-data notnull 처리
    @Operation(summary = "회원가입", description = "아이디, 비밀번호, 이름, 이메일, 금연시작 날짜, 흡연시작 날짜, 하루 흡연량, 담배 가격, 피는 담배 한갑에 있는 개수, 담배 타르양", tags = "member",
                responses = {@ApiResponse(responseCode = "201", description = "로그인 성공 후 토큰 반환"),
                            @ApiResponse(responseCode = "400", description = "아이디 또는 이메일이 중복되었습니다.")})
    @PostMapping("/member/add")
    public ResponseEntity<?> signUp(@RequestBody MemberCreateRequest request) {
        memberService.signUp(request.getUserId(), request.getPassword(), request.getName(), request.getEmail(),
                request.getNoSmk(), request.getStartSmk(), request.getAmountSmk(),
                request.getPrice(), request.getCiga(), request.getTar());
        String token = memberService.login(request.getUserId(), request.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).body(new LoginResponse(token));
    }

    @Operation(summary = "아이디 중복 조회", description = "아이디 입력하고 중복확인 눌렀을때", tags = "Check",
                responses = {@ApiResponse(responseCode = "200", description = "true : 사용 가능한 아이디입니다, false : 이미 존재하는 아이디입니다.")})
    @GetMapping("/member/id/{idCheck}")
    public ResponseEntity<Boolean> idCheck(@Parameter(description = "사용자 아이디", example = "likeLion12")@PathVariable String idCheck) {
        Member member = memberRepository.findByUserId(idCheck);
        if(member != null) return ResponseEntity.ok(false);
        return ResponseEntity.ok(true);
    }

    @Operation(summary = "이메일 중복 조회", description = "emailCheck 이메일 중복 확인", tags = "Check",
                responses = {@ApiResponse(responseCode = "200", description = "true : 사용 가능한 이메일입니다, false : 이미 존재하는 이메일입니다.")})
    @GetMapping("/member/email/{emailCheck}")
    public ResponseEntity<Boolean> emailCheck(@Parameter(description = "사용자 이메일", example = "likelion12@naver.com")@PathVariable String emailCheck) {
        Member member = memberRepository.findByEmail(emailCheck);
        if(member != null) return ResponseEntity.ok(false);
        return ResponseEntity.ok(true);
    }

    @Operation(summary = "아이디 찾기", description = "아이디 찾기 화면", tags = "member",
                responses = {@ApiResponse(responseCode = "200", description = "등록된 회원 아이디 출력"),
                            @ApiResponse(responseCode = "404", description = "false (이름과 이메일을 잘못 입력하였습니다.)")})
    @PostMapping("/member/idFind")
    public ResponseEntity<?> idFind(@RequestBody IdFindRequest request) {
        Member member = memberRepository.findByNameEmail(request.getName(), request.getEmail());
        if(member == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        return ResponseEntity.ok(member.getUserId());
    }

    @Operation(summary = "로그인", description = "회원가입 후 아이디, 패스워드 입력 후 로그인(로그인 성공 시 토큰 생성)", tags = "login",
            responses = {@ApiResponse(responseCode = "200", description = "토큰 반환"),
                    @ApiResponse(responseCode = "400", description = "Id 또는 패스워드가 잘못되었습니다.")})
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberLoginRequest request) {
        String token = memberService.login(request.getUserId(), request.getPassword());
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @Operation(summary = "임시 비밀번호 초기화", description = "userId, email 확인", tags = "member",
            responses = {@ApiResponse(responseCode = "200", description = "임시 비밀번호 해당 이메일에 전송, true"),
                        @ApiResponse(responseCode = "400", description = "아이디 또는 비밀번호가 잘못되었습니다, false")})
    @PostMapping("/member/findPassword")
    public ResponseEntity<Boolean> findPassword(@RequestBody PasswordFindRequest request) {
        Member member = memberRepository.findByUserIdAndEmail(request.getUserId(), request.getEmail());
        if(member == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);

        String password = mailService.tmpPassword();
        member.setPassword(password);

        try {
            mailService.sendTmpMail(request.getEmail(), password);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new MailSendException();
        }

        return ResponseEntity.ok(true);
    }

    @Operation(summary = "회원 탈퇴", description = "header 에 bearer 토큰 설정", tags = "member",
            responses = {@ApiResponse(responseCode = "200", description = "성공적으로 회원 탈퇴"),
                    @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰")})
    @DeleteMapping("/member")
    public ResponseEntity<String> deleteMember(@RequestHeader("Authorization") String BearerToken) {
        String token = BearerToken.replace("Bearer", "");
        memberService.deleteMember(token);
        return ResponseEntity.ok("성공적으로 회원이 탈퇴되었습니다.");
    }

    @Operation(summary = "회원 정보 수정", description = "로그인 성공 후 회원 정보 수정(이름, 금연시작일시, 흡연 시작일시, 하루 흡연량, 담배가격, 담배 한 갑당 개비수, 타르(담배), 회원 사진", tags = "member",
                responses = {@ApiResponse(responseCode = "200", description = "수정된 정보 출력")})
    @PutMapping(value = "/member", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> changeMemberInfo(@RequestHeader("Authorization") String bearerToken, @RequestPart MemberUpdateRequest request, @RequestPart(required = false) MultipartFile image) throws IOException {
        String token = bearerToken.replace("Bearer", "");
        Member findMember = memberService.changeInfo(token, request.getName(), request.getEmail(), request.getNoSmk(),
                request.getStartSmk(), request.getAmountSmk(), request.getPrice(), request.getCiga(),
                request.getTar(), image);
        String encodeImage = ImageUtility.encodeImage(findMember.getImage());
        return ResponseEntity.ok(new MemberUpdateResponse(findMember.getName(), findMember.getEmail(), findMember.getNoSmk(),
                findMember.getStartSmk(), findMember.getAmountSmk(), findMember.getPrice(), findMember.getCiga(), request.getTar(), encodeImage));
    }

    @Operation(summary = "메인화면 홈", description = "Authorization Bearer 토큰 필요, 로그인 성공 후 메인화면 페이지(내 현황(피우지않은 담배 개수(개), 절약한 금액(원), 늘어난 수명(분)), 흡연정보(총 흡연기간(일), 소비한 금액(원), 삼킨 타르 양(mg))", tags = "main",
                responses = {@ApiResponse(responseCode = "200", description = "내 현황 출력"),
                            @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰입니다.")})
    @GetMapping("/main/info")
    public ResponseEntity<?> getMainInfo(@RequestHeader("Authorization") String bearerToken) {      // 로그인 했을때 메인 페이지 출력
        String token = bearerToken.replace("Bearer ", "");
        Member member = memberService.tokentoMember(token);

        List<Object> list = new ArrayList<>();
        list.add(memberService.stateOfChange(member));
        list.add(memberService.calcNotSmoked(member));
        list.add(memberService.calcSaveMoney(member));
        list.add(memberService.calcLife(member));
        list.add(memberService.sumSmokedDate(member));
        list.add(memberService.sumMoney(member));
        list.add( memberService.sumTar(member));

        return ResponseEntity.ok(list);
    }

    @Operation(summary = "상태변화", description = "header에 Bearer 토큰 필요, 금연한지 몇시간 경과하였는지", tags = "detail",
                responses = {@ApiResponse(responseCode = "200", description = "금연한지 몇시간 경과하였는지 분으로 출력"),
                            @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰입니다.")})
    @GetMapping("/main/info/change-state")      // 금연한지 몇시간 경과하였는지
    public ResponseEntity<?> getChangeState(@RequestHeader("Authorization") String bearerToken) {
        String token = bearerToken.replace("Bearer ", "");
        Member member = memberService.tokentoMember(token);
        int state = memberService.stateOfChange(member);

        return ResponseEntity.ok(state);
    }

    @Operation(summary = "절약한 금액", description = "header에 Bearer 토큰 필요, 절약한 금액 출력", tags = "detail",
                responses = {@ApiResponse(responseCode = "200", description = "절약한 금액 출력 뒤에 원붙이면됨"),
                            @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰입니다.")})
    @GetMapping("/main/info/detail-save")      // 절약한 금액 자세히 보기
    public ResponseEntity<?> getMainInfoDetailSave(@RequestHeader("Authorization") String bearerToken) {
        String token = bearerToken.replace("Bearer ", "");
        Member member = memberService.tokentoMember(token);
        int state = memberService.stateOfChange(member);

        return ResponseEntity.ok(state);
    }

    @Operation(summary = "담배 가격", description = "Authorization Bearer 토큰 필요, 담배 가격", tags = "detail",
                responses = {@ApiResponse(responseCode = "200", description = "담배가격 출력"),
                            @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰입니다.")})
    @GetMapping("/main/info/money")
    public ResponseEntity<?> getCigaMoney(@RequestHeader("Authorization") String bearerToken) {
        String token = bearerToken.replace("Bearer ", "");
        Member member = memberService.tokentoMember(token);
        int cigaMoney = member.getPrice();

        return ResponseEntity.ok(cigaMoney);
    }

    @Operation(summary = "하루에 담배 피는 양", description = "Authorization Bearer 토큰 필요, 담배 피는 양", tags = "detail",
                    responses = {@ApiResponse(responseCode = "200", description = "담배가격 출력"),
                                @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰입니다.")})
    @GetMapping("/main/info/today")
    public ResponseEntity<?> getToday(@RequestHeader("Authorization") String bearerToken) {
        String token = bearerToken.replace("Bearer ", "");
        Member member = memberService.tokentoMember(token);
        int cigaToday = member.getCiga();

        return ResponseEntity.ok(cigaToday);
    }

    @Operation(summary = "늘어난 수명", description = "Authorization Bearer 토큰 필요, 늘어난 수명 분으로 출력", tags = "detail",
                responses = {@ApiResponse(responseCode = "200", description = "늘어난 수명 분으로 출력"),
                            @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰입니다.")})
    @GetMapping("/main/info/detail-life")      // 늘어난 수명 자세히 보기
    public ResponseEntity<?> getMainInfoDetailLife(@RequestHeader("Authorization") String bearerToken) {
        String token = bearerToken.replace("Bearer ", "");
        Member member = memberService.tokentoMember(token);
        int state = memberService.calcLife(member);

        return ResponseEntity.ok(state);
    }

    @Operation(summary = "이메일 인증 문자 보내기", description = "회원가입시 하나의 이메일만 저장가능(mail)", tags = "email",
                responses = {@ApiResponse(responseCode = "200", description = "이메일로 인증번호를 보냈습니다.")})
    @PostMapping("/mailSend")
    public ResponseEntity<String> mailSend(@RequestBody EmailRequest email) throws MessagingException, UnsupportedEncodingException {
        mailService.sendMail(email.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body("이메일로 인증번호를 보냈습니다.");
    }

    // 아직 여기 수정
    @Operation(summary = "인증번호 검증", description = "회원가입시 인증번호 맞는지 안맞는지(email ,userNumber)", tags = "email",
                responses = {@ApiResponse(responseCode = "200", description = "인증이 성공했습니다, 인증이 실패했습니다.")})
    @GetMapping("/mailCheck")
    public ResponseEntity<?> mailCheck(@RequestParam String email, @RequestParam String userNumber) {
        boolean isMatch = mailService.verifyCode(email, userNumber);

        if(isMatch) {
            return ResponseEntity.ok("인증이 성공했습니다.");
        } else {
            return ResponseEntity.ok("인증이 실패했습니다.");
        }
    }
}
