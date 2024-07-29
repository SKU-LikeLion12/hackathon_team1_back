package spring.likelionpractice.repository;

import spring.likelionpractice.domain.Member;

import java.util.List;

public interface MemberRepository {
    Member save(Member member);

    Member findById(Long id);

    Member findByUserId(String userId);

    Member findByEmail(String email);

    List<Member> findAll();

    void deleteMember(Member member);

    Member findByNameAndEmail(String name, String email);
}
