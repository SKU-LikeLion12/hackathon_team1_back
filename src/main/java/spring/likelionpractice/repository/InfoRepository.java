package spring.likelionpractice.repository;

import spring.likelionpractice.domain.Info;
import spring.likelionpractice.domain.Member;

import java.util.List;

public interface InfoRepository {
    Info save(Info info);

    Info findByMemberId(Member memberId);

    List<Info> finaAll();

    Info findById(Long infoId);
}
