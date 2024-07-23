package spring.likelionpractice.DTO;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import spring.likelionpractice.domain.Info;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
public class InfoDTO {

    @Data
    public static class InfoRequest {
        private LocalDate nosmk;
        private LocalDate startsmk;
        private int amountsmk;
        private int price;
        private int ciga;
        private int tar;
    }

    @Data
    public static class InfoResponse {
        private LocalDate nosmk;
        private LocalDate startsmk;
        private int amountsmk;
        private int price;
        private int ciga;
        private int tar;

        public InfoResponse(Info info) {
            this.startsmk = info.getStartsmk();
            this.nosmk = info.getNosmk();
            this.amountsmk = info.getAmountsmk();
            this.price = info.getPrice();
            this.ciga = info.getCiga();
            this.tar = info.getTar();
        }
    }
}
