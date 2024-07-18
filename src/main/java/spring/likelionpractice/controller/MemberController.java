package spring.likelionpractice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import spring.likelionpractice.DTO.MemberDTO.*;
import spring.likelionpractice.domain.Member;
import spring.likelionpractice.service.MemberService;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/member/add")
    public String signUp(@RequestBody MemberCreateRequest request) {
        Member member = memberService.signUp(request.getUserId(), request.getPassword(), request.getNickname());
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
        return new MemberResponse(member.getUserId(), member.getNickname());
    }

    @PutMapping("/member")
    public MemberResponse changeMemberName(@RequestBody MemberUpdateRequest request) {
        Member findMember = memberService.changeName(request.getToken(), request.getNickname());
        return new MemberResponse(findMember.getUserId(), findMember.getNickname());
    }

    @DeleteMapping("/member")
    public void deleteMember(@RequestBody MemberDeleteRequest request) {
        memberService.deleteMember(request.getToken());
    }
}
