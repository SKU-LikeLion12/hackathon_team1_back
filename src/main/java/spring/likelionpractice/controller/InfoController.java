package spring.likelionpractice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import spring.likelionpractice.DTO.InfoDTO.*;
import spring.likelionpractice.domain.Info;
import spring.likelionpractice.service.InfoService;


@RestController
@RequiredArgsConstructor
public class InfoController {

    private final InfoService infoService;

    @PostMapping("/info/add/{memberId}")
    public InfoResponse addInfo(@PathVariable Long memberId, @RequestBody InfoRequest request) {
        Info info = infoService.signupInfo(
                    memberId,
                    request.getStartsmk(),
                    request.getNosmk(),
                    request.getAmountsmk(),
                    request.getPrice(),
                    request.getCiga()
        );

        if (info == null) {
            return null;
        }

        return new InfoResponse(info);
    }
}