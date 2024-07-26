package spring.likelionpractice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import spring.likelionpractice.DTO.MemberDTO.*;
import spring.likelionpractice.domain.Member;
import spring.likelionpractice.service.MemberService;

import java.io.IOException;

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

    // 수정 필요
    @PutMapping("/member")
    public MemberResponse changeMemberInfo(@RequestParam MemberUpdateRequest request) throws IOException {
        Member findMember = memberService.changeInfo(request.getToken(), request.getName(), request.getPhone(), request.getNoSmk(),
                                                    request.getStartSmk(), request.getAmountSmk(), request.getPrice(), request.getCiga(),
                                                    request.getTar());
        return new MemberResponse(findMember.getUserId(), findMember.getName());
    }

    @DeleteMapping("/member")
    public void deleteMember(@RequestBody MemberDeleteRequest request) {
        memberService.deleteMember(request.getToken());
    }
}
