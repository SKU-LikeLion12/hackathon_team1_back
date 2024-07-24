package spring.likelionpractice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.likelionpractice.domain.Info;
import spring.likelionpractice.domain.Member;
import spring.likelionpractice.repository.InfoRepository;
import spring.likelionpractice.repository.MemberRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InfoService {

    private final LocalDate localDate = LocalDate.now();
    private final MemberRepository memberRepository;
    private final InfoRepository infoRepository;

    @Transactional
    public Info signupInfo(Long memberId, LocalDate startsmk,LocalDate nosmk, int amountsmk, int price, int ciga, int tar) {
        Member member = memberRepository.findById(memberId);
        if (member == null) return null;
        return infoRepository.save(new Info(member ,amountsmk, price, ciga, startsmk, nosmk, tar));
    }

    @Transactional
    public Info findMemberInfo(Long memberId) {         // Member 테이블의 id 값으로 Info 테이블의 대응되는 정보 조회
        Member member = memberRepository.findById(memberId);
        return infoRepository.findByMemberId(member);
    }

    @Transactional
    public List<Info> findAll() {                       // info 테이블의 모든 값 조회
        return infoRepository.finaAll();
    }

    // 내 상태변화
    @Transactional
    public int stateOfChange(Member memberId) {           // 금연한지 얼마나 지났는지 = 현재 날짜 - 금연 시작 일시 * 1440(하루 분)
        Info info = infoRepository.findByMemberId(memberId);
        LocalDate noSmkDay = info.getNosmk();
        return (int) ChronoUnit.DAYS.between(noSmkDay, localDate) * 1440;
    }
    // 내 상태변화

    // 내 성과
    @Transactional
    public int calcNotSmoked(Member memberId) {         // 피우지 않은 담배 개수 = (현재 날짜 - 금연시작 일시) * 하루 흡연량
        Info info = infoRepository.findByMemberId(memberId);        // Info 테이블 조회
        LocalDate noSmkDay = info.getNosmk();
        return (int) (ChronoUnit.DAYS.between(noSmkDay, localDate) * info.getAmountsmk());
    }

    @Transactional
    public int calcSaveMoney(Member memberId) {         // 절약한 금액 = Math.round((현재 날짜 - 금연시작 일시 * 하루 흡연량 / 한갑당 개비 수)) * 담배 가격
        Info info = infoRepository.findByMemberId(memberId);
        LocalDate noSmkDay = info.getNosmk();
        return Math.round((ChronoUnit.DAYS.between(noSmkDay, localDate) * info.getAmountsmk())) * info.getPrice();
    }

    @Transactional
    public int calcLife(Member memberId) {             // 늘어난 수명 = (현재 날짜 - 금연시작 일시) * 하루 흡연량 * 11
        Info info = infoRepository.findByMemberId(memberId);
        LocalDate noSmkDay = info.getNosmk();
        return (int) (ChronoUnit.DAYS.between(noSmkDay, localDate) * 1440 * 11);
    }
    // 내 성과

    // 과거 흡연 정보
    @Transactional
    public int sumSmokedDate(Member memberId) {        // 총 흡연기간 = (금연시작 일시 - 흡연 시작 일시)
        Info info = infoRepository.findByMemberId(memberId);
        LocalDate noSmkDay = info.getNosmk();
        return (int) ChronoUnit.DAYS.between(info.getStartsmk(), noSmkDay);
    }

    @Transactional
    public int sumMoney(Member memberId) {              // 소비한 금액 = (금연 시작 일시 - 흡연시작 일시) * 담배가격
        Info info = infoRepository.findByMemberId(memberId);
        LocalDate noSmkDay = info.getNosmk();
        return (int) ChronoUnit.DAYS.between(info.getStartsmk(), noSmkDay) * info.getPrice();
    }

    @Transactional
    public int sumTar(Member memberId) {                // 삼킨 타르양 = (금연 시작 일시 - 흡연 시작 일시) * 하루 흡연량 * 타르양
        Info info = infoRepository.findByMemberId(memberId);
        LocalDate noSmkDay = info.getNosmk();
        return (int) ChronoUnit.DAYS.between(info.getStartsmk(), noSmkDay) * info.getAmountsmk() * info.getTar();
    }
    // 과거 흡연 정보

}

