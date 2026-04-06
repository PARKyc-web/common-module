package com.parkyc.comm_module.login.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import com.parkyc.comm_module.common.JwtProvider;
import com.parkyc.comm_module.login.dto.LoginDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoginServiceImpl implements LoginService {

    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    public LoginDTO.Response login(LoginDTO.Request loginInfo){
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(
                            loginInfo.getLoginId(),
                            loginInfo.getPassword()
                    )
            );
        } catch (BadCredentialsException exception) {
            return LoginDTO.Response.builder()
                    .result(LoginDTO.Code.FAIL)
                    .loginId(loginInfo.getLoginId())
                    .build();
        }

        CustomUserDetails loginMember = (CustomUserDetails) authentication.getPrincipal();

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
