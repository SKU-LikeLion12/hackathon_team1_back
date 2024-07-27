package spring.likelionpractice.controller;

import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.likelionpractice.DTO.MemberDTO.*;
import spring.likelionpractice.domain.Member;
import spring.likelionpractice.service.ImageUtility;
import spring.likelionpractice.service.MemberService;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/member/add")
    public String signUp(@RequestBody MemberCreateRequest request) {
        Member member = memberService.signUp(request.getUserId(), request.getPassword(), request.getName(), request.getPhone(),
                                            request.getNoSmk(), request.getStartSmk(), request.getAmountSmk(),
                                            request.getPrice(), request.getCiga(), request.getTar());
        if(member == null) return null;
        return memberService.login(request.getUserId(), request.getPassword());
    }

    @PostMapping("/login")
    public String login(@RequestBody MemberLoginRequest request) {
        return memberService.login(request.getUserId(), request.getPassword());
    }

    @PostMapping("/member/{userId}")
    public MemberResponse getMember(@PathVariable("userId") String userId) {
        Member member = memberService.findByUserId(userId);
        return new MemberResponse(member.getUserId(), member.getName());
    }

    // RequestPart를 사용하여 form-data 입력을 받아야함,
    // postman form-data로 send해야함
    @PutMapping(value = "/member", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MemberUpdateResponse> changeMemberInfo(@RequestPart(name = "request") MemberUpdateRequest request, @RequestPart(required = false, name = "image") MultipartFile image) throws IOException {
        Member findMember = memberService.changeInfo(request.getToken(), request.getName(), request.getPhone(), request.getNoSmk(),
                                                    request.getStartSmk(), request.getAmountSmk(), request.getPrice(), request.getCiga(),
                                                    request.getTar(), image);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MemberUpdateResponse(request.getName(), request.getPhone(), request.getNoSmk(),
                                                                request.getStartSmk(), request.getAmountSmk(), request.getPrice(), request.getCiga(), request.getTar(), request.getImage()));
    }

    @DeleteMapping("/member")
    public void deleteMember(@RequestBody MemberDeleteRequest request) {
        memberService.deleteMember(request.getToken());
    }

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

    @GetMapping("/main/info/change-state")      // 금연한지 몇시간 경과하였는지
    public Map<String, Object> getChangeState(@RequestHeader("Authorization") String bearerToken) {
        String token = bearerToken.replace("Bearer ", "");
        Member member = memberService.tokentoMember(token);

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("금연한지 경과한 시간", memberService.stateOfChange(member));

        return map;
    }

    @GetMapping("/main/info/detail-save")      // 절약한 금액 자세히 보기
    public Map<String, Object> getMainInfoDetailSave(@RequestHeader("Authorization") String bearerToken) {
        String token = bearerToken.replace("Bearer ", "");
        Member member = memberService.tokentoMember(token);

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("절약한 금액", memberService.calcSaveMoney(member));

        return map;
    }

    @GetMapping("/main/info/detail-life")      // 늘어난 수명 자세히 보기
    public Map<String, Object> getMainInfoDetailLife(@RequestHeader("Authorization") String bearerToken) {
        String token = bearerToken.replace("Bearer ", "");
        Member member = memberService.tokentoMember(token);

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("늘어난 수명", memberService.calcLife(member));

        return map;
    }
}
