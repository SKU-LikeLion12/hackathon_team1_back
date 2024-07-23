package spring.likelionpractice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.likelionpractice.domain.Info;
import spring.likelionpractice.domain.Member;
import spring.likelionpractice.repository.InfoRepository;
import spring.likelionpractice.repository.MemberRepository;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InfoService {

    private final MemberRepository memberRepository;
    private final InfoRepository infoRepository;

    @Transactional
    public Info signupInfo(Long memberId, LocalDate startsmk,LocalDate nosmk, int amountsmk, int price, int ciga) {
        Member member = memberRepository.findById(memberId);
        if (member == null) return null;
        return infoRepository.save(new Info(member ,amountsmk, price, ciga, startsmk, nosmk));
    }
}

