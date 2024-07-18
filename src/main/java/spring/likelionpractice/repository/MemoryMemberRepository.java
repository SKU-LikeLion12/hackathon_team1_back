package spring.likelionpractice.repository;

import org.springframework.stereotype.Repository;
import spring.likelionpractice.domain.Member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryMemberRepository implements MemberRepository {

    private static final Map<Long, Member> local = new HashMap<>();

    @Override
    public Member save(Member member) {
        local.put(member.getId(), member);
        return member;
    }

    @Override
    public Member findById(Long id) {
        return local.get(id);
    }

    @Override
    public Member findByUserId(String userId) {
        for (Member member : local.values()) {
            if (member.getUserId().equals(userId)) {
                return member;
            }
        }
        return null;
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(local.values());
    }

    @Override
    public void deleteMember(Member member) {
        local.remove(member);
    }

    @Override
    public List<Member> findByName(String name) {
        List<Member> findMembers = new ArrayList<>();

        for(Member member : local.values()) {
            if (member.getNickname().equals(name)) {
                findMembers.add(member);
            }
        }
        return findMembers;
    }
}
