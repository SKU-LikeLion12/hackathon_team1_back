package spring.likelionpractice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.likelionpractice.domain.Info;
import spring.likelionpractice.repository.InfoRepository;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InfoService {

    private final InfoRepository infoRepository;

    @Transactional
    public Info signupInfo(Long id, LocalDate startsmk,LocalDate nosmk, int amountsmk, int price, int ciga) {
        Info info = infoRepository.findById(id);
        if (info != null) return null;
        return infoRepository.save(new Info(amountsmk, price, ciga, startsmk, nosmk));
    }


}

