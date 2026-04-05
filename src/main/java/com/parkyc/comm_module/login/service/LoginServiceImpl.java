package com.parkyc.comm_module.login.service;

import com.parkyc.comm_module.common.JwtProvider;
import com.parkyc.comm_module.login.dto.LoginDTO;
import com.parkyc.comm_module.member.domain.dto.MemberDTO;
import com.parkyc.comm_module.member.domain.entity.Member;
import com.parkyc.comm_module.member.repository.MemberRepository;
import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoginServiceImpl implements LoginService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public LoginDTO.Response login(LoginDTO.Request loginInfo){

        Member loginMember = memberRepository.findByLoginIdAndPassword(loginInfo.getLoginId(), loginInfo.getPassword());
        if(loginMember == null){
            return LoginDTO.Response.builder()
                    .result(LoginDTO.Code.FAIL)
                    .loginId(loginInfo.getLoginId())
                    .build();
        }

        LoginDTO.Member verifyMember = LoginDTO.Member.builder()
                .loginId(loginMember.getLoginId())
                .status(loginMember.getStatus().name())
                .build();
        JwtProvider.Response jwtResponse = jwtProvider.renewLoginToken(verifyMember);

        return LoginDTO.Response.builder()
                .result(LoginDTO.Code.SUCCESS)
                .loginId(verifyMember.getLoginId())
                .accessToken(jwtResponse.getAccessToken())
                .refreshToken(jwtResponse.getRefreshToken())
                .build();
    }
}
