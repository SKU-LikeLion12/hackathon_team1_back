package spring.likelionpractice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.likelionpractice.domain.Member;
import spring.likelionpractice.repository.MemberRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final LocalDate localDate = LocalDate.now();
    private final MemberRepository memberRepository;
    private final JwtUtility jwtUtility;

    public Member tokentoMember(String token) {
        return memberRepository.findByUserId(jwtUtility.validateToken(token).getSubject());
    }

    @Transactional
    public Member changeInfo(String token, String name, String phone, LocalDate noSmk, LocalDate startSmk,
                             int amountSmk, int price, int ciga, int tar) {
        Member member = tokentoMember(token);
        if (member == null) return null;
        member.setName(name);
        member.setPhone(phone);
        member.setNoSmk(noSmk);
        member.setStartSmk(startSmk);
        member.setAmountSmk(amountSmk);
        member.setPrice(price);
        member.setCiga(ciga);
        member.setTar(tar);
        return member;
    }

    @Transactional
    public Member signUp(String userId, String password, String name, String phone,
                         LocalDate noSmk, LocalDate startSmk, int amountSmk,
                         int price, int ciga, int tar) {
        Member member = memberRepository.findByUserId(userId);
        if(member != null) return null;
        return memberRepository.save(new Member(userId, password, name, phone, noSmk, startSmk, amountSmk, price, ciga, tar));
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

    // 내 상태변화
    @Transactional
    public int stateOfChange(String token) {            // 금연한지 얼마나 지났는지
        Member member = tokentoMember(token);
        LocalDate noSmkDay = member.getNoSmk();
        return (int) ChronoUnit.DAYS.between(noSmkDay, localDate) * 1440;
    }

    @Transactional
    public int calcNotSmoked(String token) {            // 피우지 않은 담배 개수 = (현재 날짜 - 금연시작 일시) * 하루 흡연량
        Member member = tokentoMember(token);
        LocalDate noSmkDay = member.getNoSmk();
        return (int) (ChronoUnit.DAYS.between(noSmkDay, localDate) * member.getAmountSmk());
    }

    @Transactional
    public int calcSaveMoney(String token) {            // 절약한 금액 = Math.round((현재 날짜 - 금연시작 일시 * 하루 흡연량 / 한갑당 개비 수)) * 담배 가격
        Member member = tokentoMember(token);
        LocalDate noSmkDay = member.getNoSmk();
        return Math.round((ChronoUnit.DAYS.between(noSmkDay, localDate) * member.getAmountSmk())) * member.getPrice();
    }

    @Transactional
    public int calcLife(String token) {             // 늘어난 수명 = (현재 날짜 - 금연시작 일시) * 하루 흡연량 * 11
        Member member = tokentoMember(token);
        LocalDate noSmkDay = member.getNoSmk();
        return (int) (ChronoUnit.DAYS.between(noSmkDay, localDate) * 1440 * 11);
    }
    // 내 성과

    // 과거 흡연 정보
    @Transactional
    public int sumSmokedDate(String token) {        // 총 흡연기간 = (금연시작 일시 - 흡연 시작 일시)
        Member member = tokentoMember(token);
        LocalDate noSmkDay = member.getNoSmk();
        return (int) ChronoUnit.DAYS.between(member.getStartSmk(), noSmkDay);
    }

    @Transactional
    public int sumMoney(String token) {              // 소비한 금액 = (금연 시작 일시 - 흡연시작 일시) * 담배가격
        Member member = tokentoMember(token);
        LocalDate noSmkDay = member.getNoSmk();
        return (int) ChronoUnit.DAYS.between(member.getStartSmk(), noSmkDay) * member.getPrice();
    }

    @Transactional
    public int sumTar(String token) {                // 삼킨 타르양 = (금연 시작 일시 - 흡연 시작 일시) * 하루 흡연량 * 타르양
        Member member = tokentoMember(token);
        LocalDate noSmkDay = member.getNoSmk();
        return (int) ChronoUnit.DAYS.between(member.getStartSmk(), noSmkDay) * member.getAmountSmk() * member.getTar();
    }
    // 과거 흡연 정보
}
