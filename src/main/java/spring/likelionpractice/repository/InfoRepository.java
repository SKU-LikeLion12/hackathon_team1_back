package spring.likelionpractice.repository;

import spring.likelionpractice.domain.Info;

import java.util.List;

public interface InfoRepository {
    Info save(Info info);

    Info findById(Long id);

    List<Info> finaAll();
}
