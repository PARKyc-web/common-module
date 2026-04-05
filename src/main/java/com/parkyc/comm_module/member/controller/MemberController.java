package com.parkyc.comm_module.member.controller;

import com.parkyc.comm_module.common.code.MemberStatus;
import com.parkyc.comm_module.member.domain.dto.MemberDTO;
import com.parkyc.comm_module.member.domain.entity.Member;
import com.parkyc.comm_module.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
@RestController
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/check-dup-id")
    public boolean chkDuplicateId(String loginId){

        return memberService.isDuplicateId(loginId);
    }

    // api/v1/member/sign-up
    @PostMapping("/sign-up")
    public MemberDTO.All signUp(MemberDTO.SignUp signUpInfo){

        return memberService.signUp(signUpInfo);
    }
}
