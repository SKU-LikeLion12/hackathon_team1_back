package spring.likelionpractice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import spring.likelionpractice.domain.Member;
import spring.likelionpractice.repository.MemberRepository;

import java.io.IOException;
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
                             int amountSmk, int price, int ciga, int tar, MultipartFile image) throws IOException {
        Member member = tokentoMember(token);
        if (member == null) return null;

        byte[] images = null;
        if (image != null) {
            // 이미지를 수정했을때
            images = image.getBytes();      // IOException 예외 처리를 해줘야함
        } else {
            // 이미지를 수정하지 않았을때
            images = member.getImage();
        }
        member.setName(name);
        member.setPhone(phone);
        member.setNoSmk(noSmk);
        member.setStartSmk(startSmk);
        member.setAmountSmk(amountSmk);
        member.setPrice(price);
        member.setCiga(ciga);
        member.setTar(tar);
        member.setImage(images);
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
    public int stateOfChange(Member memberId) {            // 금연한지 얼마나 지났는지
        Member member = findById(memberId.getId());
        LocalDate noSmkDay = member.getNoSmk();
        return (int) ChronoUnit.DAYS.between(noSmkDay, localDate) * 1440;
    }

    @Transactional
    public int calcNotSmoked(Member memberId) {            // 피우지 않은 담배 개수 = (현재 날짜 - 금연시작 일시) * 하루 흡연량
        Member member = findById(memberId.getId());
        LocalDate noSmkDay = member.getNoSmk();
        return (int) (ChronoUnit.DAYS.between(noSmkDay, localDate) * member.getAmountSmk());
    }

    @Transactional
    public int calcSaveMoney(Member memberId) {            // 절약한 금액 = Math.round((현재 날짜 - 금연시작 일시 * 하루 흡연량 / 한갑당 개비 수)) * 담배 가격
        Member member = findById(memberId.getId());
        LocalDate noSmkDay = member.getNoSmk();
        return Math.round((ChronoUnit.DAYS.between(noSmkDay, localDate) * member.getAmountSmk())) * member.getPrice();
    }

    @Transactional
    public int calcLife(Member memberId) {             // 늘어난 수명 = (현재 날짜 - 금연시작 일시) * 하루 흡연량 * 11
        Member member = findById(memberId.getId());
        LocalDate noSmkDay = member.getNoSmk();
        return (int) (ChronoUnit.DAYS.between(noSmkDay, localDate) * 1440 * 11);
    }
    // 내 성과

    // 과거 흡연 정보
    @Transactional
    public int sumSmokedDate(Member memberId) {        // 총 흡연기간 = (금연시작 일시 - 흡연 시작 일시)
        Member member = findById(memberId.getId());
        LocalDate noSmkDay = member.getNoSmk();
        return (int) ChronoUnit.DAYS.between(member.getStartSmk(), noSmkDay);
    }

    @Transactional
    public int sumMoney(Member memberId) {              // 소비한 금액 = (금연 시작 일시 - 흡연시작 일시) * 담배가격
        Member member = findById(memberId.getId());
        LocalDate noSmkDay = member.getNoSmk();
        return (int) ChronoUnit.DAYS.between(member.getStartSmk(), noSmkDay) * member.getPrice();
    }

    @Transactional
    public int sumTar(Member memberId) {                // 삼킨 타르양 = (금연 시작 일시 - 흡연 시작 일시) * 하루 흡연량 * 타르양
        Member member = findById(memberId.getId());
        LocalDate noSmkDay = member.getNoSmk();
        return (int) ChronoUnit.DAYS.between(member.getStartSmk(), noSmkDay) * member.getAmountSmk() * member.getTar();
    }
    // 과거 흡연 정보
}
