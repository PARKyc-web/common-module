package com.parkyc.comm_module.member.service;

import com.parkyc.comm_module.member.domain.dto.MemberDTO;

public interface MemberService {

    boolean isDuplicateId(String loginId);

    MemberDTO.All signUp(MemberDTO.SignUp signUpInfo);
}
