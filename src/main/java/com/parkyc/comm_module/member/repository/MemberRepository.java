package com.parkyc.comm_module.member.repository;

import com.parkyc.comm_module.member.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByLoginIdAndPassword(String loginId, String password);

    int countMemberByLoginId(String loginId);

    Member findMemberByLoginId(String loginId);
}
