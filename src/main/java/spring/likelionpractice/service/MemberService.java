package spring.likelionpractice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.likelionpractice.domain.Member;
import spring.likelionpractice.repository.MemberRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtility jwtUtility;

    public Member tokentoMember(String token) {
        return memberRepository.findByUserId(jwtUtility.validateToken(token).getSubject());
    }

    @Transactional
    public Member changeName(String token, String name) {
        Member member = tokentoMember(token);
        if (member == null) return null;
        member.setName(name);
        return member;
    }

    @Transactional
    public Member signUp(String userId, String password, String name, String phone) {
        Member member = memberRepository.findByUserId(userId);
        if(member != null) return null;
        return memberRepository.save(new Member(userId, password, name, phone));
    }

    public Member findById(Long id) {
        return memberRepository.findById(id);
    }

    public String login(String userId, String password) {
        Member member = findByUserId(userId);
        if(member != null && member.checkPassword(password)) {
            return jwtUtility.generateToken(member.getUserId());
        }
        return null;
    }

    public Member findByUserId(String userId) {
        return memberRepository.findByUserId(userId);
    }

    public List<Member> findByName(String name) {
        return memberRepository.findByName(name);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public boolean deleteMember(String token) {
        Member member = tokentoMember(token);
        if(member == null) return false;
        memberRepository.deleteMember(member);
        return true;
    }
}
