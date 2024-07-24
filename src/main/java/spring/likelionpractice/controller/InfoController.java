package spring.likelionpractice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import spring.likelionpractice.DTO.InfoDTO.*;
import spring.likelionpractice.domain.Info;
import spring.likelionpractice.domain.Member;
import spring.likelionpractice.service.InfoService;
import spring.likelionpractice.service.MemberService;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class InfoController {

    private final InfoService infoService;
    private final MemberService memberService;

    @PostMapping("/info/add/{memberId}")
    public InfoResponse addInfo(@PathVariable Long memberId, @RequestBody InfoRequest request) {
        Info info = infoService.signupInfo(
                    memberId,
                    request.getStartsmk(),
                    request.getNosmk(),
                    request.getAmountsmk(),
                    request.getPrice(),
                    request.getCiga(),
                    request.getTar()
        );

        if (info == null) {
            return null;
        }

        return new InfoResponse(info);
    }

    // Get 요청을 했을시 가지고 있는 토큰 값을 가지고 출력해야함
    @PostMapping("/main/info")
    public Map<String, Object> getMainInfo(@RequestBody mainRequest request) {
        Member member = memberService.tokentoMember(request.getToken());

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("금연한지 얼마나 지났는지", infoService.stateOfChange(member));
        map.put("피우지 않은 담배 개수", infoService.calcNotSmoked(member));
        map.put("절약한 금액", infoService.calcSaveMoney(member));
        map.put("늘어난 수명", infoService.calcLife(member));
        map.put("총 흡연기간", infoService.sumSmokedDate(member));
        map.put("소비한 금액", infoService.sumMoney(member));
        map.put("삼킨 타르양", infoService.sumTar(member));

        return map;
    }

    @PostMapping("main/info/change-state")      // 금연한지 몇시간 경과하였는지
    public Map<String, Object> getChangeState(@RequestBody mainRequest request) {
        Member member = memberService.tokentoMember(request.getToken());

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("금연한지 경과한 시간", infoService.stateOfChange(member));

        return map;
    }

    @PostMapping("/main/info/detail-save")      // 절약한 금액 자세히 보기
    public Map<String, Object> getMainInfoDetailSave(@RequestBody mainRequest request) {
        Member member = memberService.tokentoMember(request.getToken());

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("절약한 금액", infoService.calcSaveMoney(member));

        return map;
    }

    @PostMapping("/main/info/detail-life")      // 늘어난 수명 자세히 보기
    public Map<String, Object> getMainInfoDetailLife(@RequestBody mainRequest request) {
        Member member = memberService.tokentoMember(request.getToken());

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("늘어난 수명", infoService.calcLife(member));

        return map;
    }
}
