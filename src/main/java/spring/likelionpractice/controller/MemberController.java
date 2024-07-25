package spring.likelionpractice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.likelionpractice.DTO.MemberDTO.*;
import spring.likelionpractice.domain.Member;
import spring.likelionpractice.service.MemberService;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/member/find-id")
    public ResponseEntity<String> findUserId(@RequestParam String phone) {
        return memberService.findUserIdByPhone(phone)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/member/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestParam String userId, @RequestParam String newPassword) {
        if (memberService.resetPassword(userId, newPassword)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/member/add")
    public String signUp(@RequestBody MemberCreateRequest request) {
        Member member = memberService.signUp(request.getUserId(), request.getPassword(),request.getPhone(), request.getFilename());
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
