package spring.likelionpractice.controller;

import io.swagger.v3.oas.annotations.Operation;
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
import spring.likelionpractice.domain.Member;
import spring.likelionpractice.repository.MemberRepository;
import spring.likelionpractice.service.ImageUtility;
import spring.likelionpractice.service.MemberService;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;
    private final MemberService memberService;


    @Operation(summary = "회원가입", description = "아이디, 비밀번호, 이름, 이메일, 금연시작 날짜, 흡연시작 날짜, 하루 흡연량, 담배 가격, 피는 담배 한갑에 있는 개수, 담배 타르양", tags = "signup")
    @PostMapping("/member/add")
    public String signUp(@RequestBody MemberCreateRequest request) {
        Member member = memberService.signUp(request.getUserId(), request.getPassword(), request.getName(), request.getEmail(),
                                            request.getNoSmk(), request.getStartSmk(), request.getAmountSmk(),
                                            request.getPrice(), request.getCiga(), request.getTar());
        if(member == null) return null;
        return memberService.login(request.getUserId(), request.getPassword());
    }

    // 아이디 중복 체크
    // 이메일도 해야함
    @Operation(summary = "아이디 중복 조회", description = "아이디 입력하고 중복확인 눌렀을때", tags = "idCheck")
    @GetMapping("/member/{idCheck}")
    public String idCheck(@PathVariable String idCheck) {
        Member member = memberRepository.findByUserId(idCheck);
        if(member != null) return "이미 존재하는 아이디입니다.";
        return "사용 가능한 아이디입니다.";
    }

    @Operation(summary = "로그인", description = "회원가입 후 아이디, 패스워드 입력 후 로그인(로그인 성공 시 토큰 생성)", tags = "login")
    @PostMapping("/login")
    public String login(@RequestBody MemberLoginRequest request) {
        return memberService.login(request.getUserId(), request.getPassword());
    }

    @Operation(summary = "회원 탈퇴", description = "로그인 성공 후 회원 탈퇴(토큰 값으로 회원을 찾아 삭제)", tags = "member delete")
    @DeleteMapping("/member")
    public void deleteMember(@RequestBody MemberDeleteRequest request) {
        memberService.deleteMember(request.getToken());
    }

    // RequestPart를 사용하여 form-data 입력을 받아야함,
    // postman form-data로 send해야함
    @Operation(summary = "회원 정보 수정", description = "로그인 성공 후 회원 정보 수정(이름, 금연시작일시, 흡연 시작일시, 하루 흡연량, 담배가격, 담배 한 갑당 개비수, 타르(담배), 회원 사진", tags = "member put")
    @PutMapping(value = "/member", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MemberUpdateResponse> changeMemberInfo(@RequestPart(name = "request") MemberUpdateRequest request, @RequestPart(required = false, name = "image") MultipartFile image) throws IOException {
        Member findMember = memberService.changeInfo(request.getToken(), request.getName(), request.getEmail(), request.getNoSmk(),
                request.getStartSmk(), request.getAmountSmk(), request.getPrice(), request.getCiga(),
                request.getTar(), image);
        String encodeImage = ImageUtility.encodeImage(findMember.getImage());
        return ResponseEntity.status(HttpStatus.CREATED).body(new MemberUpdateResponse(findMember.getName(), findMember.getEmail(), findMember.getNoSmk(),
                findMember.getStartSmk(), findMember.getAmountSmk(), findMember.getPrice(), findMember.getCiga(), request.getTar(), encodeImage));
    }

    // 헤더에 토큰 넣어서 Bearer
    @Operation(summary = "메인화면 홈", description = "로그인 성공 후 메인화면 페이지(내 현황(피우지않은 담배 개수(개), 절약한 금액(원), 늘어난 수명(분)), 흡연정보(총 흡연기간(일), 소비한 금액(원), 삼킨 타르 양(mg))", tags = "mainInfo")
    @GetMapping("/member/info")
    public Map<String, Object> getMainInfo(@RequestHeader("Authorization") String bearerToken) {      // 로그인 했을때 메인 페이지 출력
        String token = bearerToken.replace("Bearer ", "");
        Member member = memberService.tokentoMember(token);

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("금연한지 얼마나 지났는지", memberService.stateOfChange(member));
        map.put("피우지 않은 담배 개수", memberService.calcNotSmoked(member));
        map.put("절약한 금액", memberService.calcSaveMoney(member));
        map.put("늘어난 수명", memberService.calcLife(member));
        map.put("총 흡연기간", memberService.sumSmokedDate(member));
        map.put("소비한 금액", memberService.sumMoney(member));
        map.put("삼킨 타르양", memberService.sumTar(member));

        return map;
    }

    @Operation(summary = "상태변화", description = "로그인 성공 후 메인-내 상태변화(금연한지 몇시간 지났는지(분))", tags = "stateChange")
    @GetMapping("/main/info/change-state")      // 금연한지 몇시간 경과하였는지
    public Map<String, Object> getChangeState(@RequestHeader("Authorization") String bearerToken) {
        String token = bearerToken.replace("Bearer ", "");
        Member member = memberService.tokentoMember(token);

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("금연한지 경과한 시간", memberService.stateOfChange(member));

        return map;
    }

    @Operation(summary = "절약한 금액", description = "로그인 성공 후 절약한 금액(원) - 자세히보기", tags = "detailSaveMoney")
    @GetMapping("/main/info/detail-save")      // 절약한 금액 자세히 보기
    public Map<String, Object> getMainInfoDetailSave(@RequestHeader("Authorization") String bearerToken) {
        String token = bearerToken.replace("Bearer ", "");
        Member member = memberService.tokentoMember(token);

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("절약한 금액", memberService.calcSaveMoney(member));

        return map;
    }

    @Operation(summary = "늘어난 수명", description = "로그인 성공 후 늘어난 수명(분) - 자세히보기", tags = "detailLife")
    @GetMapping("/main/info/detail-life")      // 늘어난 수명 자세히 보기
    public Map<String, Object> getMainInfoDetailLife(@RequestHeader("Authorization") String bearerToken) {
        String token = bearerToken.replace("Bearer ", "");
        Member member = memberService.tokentoMember(token);

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("늘어난 수명", memberService.calcLife(member));

        return map;
    }

    @PostMapping("/member/{userId}")
    public MemberResponse getMember(@PathVariable("userId") String userId) {
        Member member = memberService.findByUserId(userId);
        return new MemberResponse(member.getUserId(), member.getName());
    }
}
