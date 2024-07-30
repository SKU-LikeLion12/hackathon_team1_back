package spring.likelionpractice.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.likelionpractice.DTO.MemberDTO.*;
import spring.likelionpractice.Exception.InvalidCredentialsException;
import spring.likelionpractice.domain.Member;
import spring.likelionpractice.repository.MemberRepository;
import spring.likelionpractice.service.ImageUtility;
import spring.likelionpractice.service.MailService;
import spring.likelionpractice.service.MemberService;

import java.io.IOException;
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
    @Operation(summary = "회원가입", description = "아이디, 비밀번호, 이름, 이메일, 금연시작 날짜, 흡연시작 날짜, 하루 흡연량, 담배 가격, 피는 담배 한갑에 있는 개수, 담배 타르양", tags = "member")
    @PostMapping("/member/add")
    public ResponseEntity<String> signUp(@RequestBody MemberCreateRequest request) {
        try {
            Member member = memberService.signUp(request.getUserId(), request.getPassword(), request.getName(), request.getEmail(),
                    request.getNoSmk(), request.getStartSmk(), request.getAmountSmk(),
                    request.getPrice(), request.getCiga(), request.getTar());
            if (member == null) return ResponseEntity.status(HttpStatus.FORBIDDEN).body("이미 존재하는 아이디이거나 이메일입니다.");
            String token = memberService.login(request.getUserId(), request.getPassword());
            return ResponseEntity.status(HttpStatus.OK).body(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입에 실패하였습니다.");
        }
    }

    @Operation(summary = "아이디 중복 조회", description = "아이디 입력하고 중복확인 눌렀을때", tags = "Check")
    @GetMapping("/member/{idCheck}")
    public String idCheck(@PathVariable String idCheck) {
        Member member = memberRepository.findByUserId(idCheck);
        if(member != null) return "이미 존재하는 아이디입니다.";
        return "사용 가능한 아이디입니다.";
    }

    @Operation(summary = "이메일 중복 조회", description = "emailCheck 이메일 중복 확인", tags = "Check")
    @GetMapping("/member/{emailCheck}")
    public String emailCheck(@PathVariable String emailCheck) {
        Member member = memberRepository.findByEmail(emailCheck);
        if(member != null) return "이미 존재하는 이메일입니다.";
        return "사용 가능한 이메일입니다.";
    }

    @Operation(summary = "로그인", description = "회원가입 후 아이디, 패스워드 입력 후 로그인(로그인 성공 시 토큰 생성)", tags = "login")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberLoginRequest request) {
        try {
            String token = memberService.login(request.getUserId(), request.getPassword());
            return ResponseEntity.ok(new LoginResponse(token));
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 유효하지 않습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("발견 못한 오류입니다.");
        }
    }

    @Operation(summary = "비밀번호 초기화하기전", description = "userId, email 확인", tags = "member")
    @PostMapping("/member/findPassword")
    public String findPassword(@RequestParam String userId,@RequestParam String email, HttpSession session) {
        Member member = memberRepository.findByUserIdAndEmail(userId, email);
        if(member == null) return "아이디 또는 이메일이 잘못되었습니다.";
        session.setAttribute("resetPasswordMemberId", member.getId());
        return "redirect:/member/resetPassword";
    }

    @Operation(summary = "비밀번호 초기화", description = "새로운 password 생성", tags = "member")
    @PostMapping("/member/resetPassword")
    public String resetPassword(@RequestParam String password, HttpSession session) {
        Long memberId = (Long) session.getAttribute("resetPasswordMemberId");
        if (memberId == null) {
            return "비밀번호 찾기 절차를 다시 수행해주세요";
        }
        Member member = memberRepository.findById(memberId);
        member.setPassword(password);
        memberRepository.save(member);
        session.removeAttribute("resetPasswordMemberId");
        return "비밀번호가 성공적으로 재설정되었습니다.";
    }

    @Operation(summary = "회원 탈퇴", description = "로그인 성공 후 회원 탈퇴(토큰 값으로 회원을 찾아 삭제)", tags = "member")
    @DeleteMapping("/member")
    public ResponseEntity<?> deleteMember(@RequestBody MemberDeleteRequest request) {
        try {
            memberService.deleteMember(request.getToken());
            return ResponseEntity.ok().build();
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 유효하지 않습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("발견 못한 오류입니다.");
        }
    }

    // RequestPart를 사용하여 form-data 입력을 받아야함,
    // postman form-data로 send해야함
    @Operation(summary = "회원 정보 수정", description = "로그인 성공 후 회원 정보 수정(이름, 금연시작일시, 흡연 시작일시, 하루 흡연량, 담배가격, 담배 한 갑당 개비수, 타르(담배), 회원 사진", tags = "member")
    @PutMapping(value = "/member", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> changeMemberInfo(@RequestPart(name = "request") MemberUpdateRequest request, @RequestPart(required = false, name = "image") MultipartFile image) throws IOException {
        try {
            Member findMember = memberService.changeInfo(request.getToken(), request.getName(), request.getEmail(), request.getNoSmk(),
                    request.getStartSmk(), request.getAmountSmk(), request.getPrice(), request.getCiga(),
                    request.getTar(), image);
            String encodeImage = ImageUtility.encodeImage(findMember.getImage());
            return ResponseEntity.status(HttpStatus.CREATED).body(new MemberUpdateResponse(findMember.getName(), findMember.getEmail(), findMember.getNoSmk(),
                    findMember.getStartSmk(), findMember.getAmountSmk(), findMember.getPrice(), findMember.getCiga(), request.getTar(), encodeImage));
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 유효하지 않습니다.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미지 처리 오류입니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("발견못한 오류입니다.");
        }
    }

    // 헤더에 토큰 넣어서 Bearer
    @Operation(summary = "메인화면 홈", description = "로그인 성공 후 메인화면 페이지(내 현황(피우지않은 담배 개수(개), 절약한 금액(원), 늘어난 수명(분)), 흡연정보(총 흡연기간(일), 소비한 금액(원), 삼킨 타르 양(mg))", tags = "main")
    @GetMapping("/member/info")
    public ResponseEntity<?> getMainInfo(@RequestHeader("Authorization") String bearerToken) {      // 로그인 했을때 메인 페이지 출력
        try {
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
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 유효하지 않습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("발견못한 오류입니다.");
        }
    }

    @Operation(summary = "상태변화", description = "로그인 성공 후 메인-내 상태변화(금연한지 몇시간 지났는지(분))", tags = "detail")
    @GetMapping("/main/info/change-state")      // 금연한지 몇시간 경과하였는지
    public ResponseEntity<?> getChangeState(@RequestHeader("Authorization") String bearerToken) {
        try {
            String token = bearerToken.replace("Bearer ", "");
            Member member = memberService.tokentoMember(token);
            int state = memberService.stateOfChange(member);

            return ResponseEntity.ok(state);
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 유효하지 않습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("발견못한 오류입니다.");
        }
    }

    @Operation(summary = "절약한 금액", description = "로그인 성공 후 절약한 금액(원) - 자세히보기", tags = "detail")
    @GetMapping("/main/info/detail-save")      // 절약한 금액 자세히 보기
    public ResponseEntity<?> getMainInfoDetailSave(@RequestHeader("Authorization") String bearerToken) {
        try {
            String token = bearerToken.replace("Bearer ", "");
            Member member = memberService.tokentoMember(token);
            int state = memberService.stateOfChange(member);

            return ResponseEntity.ok(state);
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 유효하지 않습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("발견못한 오류입니다.");
        }
    }

    @Operation(summary = "늘어난 수명", description = "로그인 성공 후 늘어난 수명(분) - 자세히보기", tags = "detail")
    @GetMapping("/main/info/detail-life")      // 늘어난 수명 자세히 보기
    public ResponseEntity<?> getMainInfoDetailLife(@RequestHeader("Authorization") String bearerToken) {
        try {
            String token = bearerToken.replace("Bearer ", "");
            Member member = memberService.tokentoMember(token);
            int state = memberService.stateOfChange(member);

            return ResponseEntity.ok(state);
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 유효하지 않습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("발견못한 오류입니다.");
        }
    }

    @Operation(summary = "이메일 인증 문자 보내기", description = "회원가입시 하나의 이메일만 저장가능(mail)", tags = "email")
    @PostMapping("/mailSend")
    public ResponseEntity<String> mailSend(@RequestParam String mail) {

        try {
            mailService.sendMail(mail);
            return ResponseEntity.status(HttpStatus.OK).body("이메일로 인증번호를 보냈습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("발견못한 오류입니다.");
        }
    }

    @Operation(summary = "이메일 인증번호 체크", description = "회원가입시 인증번호 맞는지 안맞는지(userNumber)", tags = "email")
    @GetMapping("/mailCheck")
    public ResponseEntity<?> mailCheck(@RequestParam String userNumber) {
        try {
            boolean isMatch = userNumber.equals(String.valueOf(number));

            return ResponseEntity.ok(isMatch);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/member/{userId}")
    public MemberResponse getMember(@PathVariable("userId") String userId) {
        Member member = memberService.findByUserId(userId);
        return new MemberResponse(member.getUserId(), member.getName());
    }
}
