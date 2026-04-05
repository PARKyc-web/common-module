package com.parkyc.comm_module.member.service;

import com.parkyc.comm_module.common.code.MemberStatus;
import com.parkyc.comm_module.member.domain.dto.MemberDTO;
import com.parkyc.comm_module.member.domain.entity.Member;
import com.parkyc.comm_module.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    @Override
    public boolean isDuplicateId(String loginId) {
        int cnt = memberRepository.countMemberByLoginId(loginId);

        return !(cnt > 0);
    }

    @Override
    public MemberDTO.All signUp(MemberDTO.SignUp signUpInfo) {

        boolean isDup = isDuplicateId(signUpInfo.getLoginId());

        if(!isDup){
            return null;
        }
        Member member = Member.builder()
                .loginId(signUpInfo.getLoginId())
                .password(signUpInfo.getPassword())
                .status(MemberStatus.ACTIVE)
                .build();
        memberRepository.saveAndFlush(member);

        return MemberDTO.All.builder()
                .loginId(member.getLoginId())
                .password(member.getPassword())
                .status(member.getStatus().name())
                .regDt(member.getRegDt())
                .updDt(member.getUpdDt())
                .build();
    }
}
