package spring.likelionpractice.repository;

import spring.likelionpractice.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);

    Member findById(Long id);

    Member findByUserId(String userId);

    Optional<Member> findByPhone(String phone);

    List<Member> findAll();

    void deleteMember(Member member);

    List<Member> findByName(String name);

}
