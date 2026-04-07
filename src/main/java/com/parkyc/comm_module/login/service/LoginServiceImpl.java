package com.parkyc.comm_module.login.service;

import com.parkyc.comm_module.common.JwtProvider;
import com.parkyc.comm_module.login.dto.LoginDTO;
import com.parkyc.comm_module.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoginServiceImpl implements LoginService {

    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    public JwtProvider.Response webLogin(LoginDTO.Request loginInfo){
        return authenticateAndIssueToken(loginInfo);
    }

    @Override
    public JwtProvider.Response appLogin(LoginDTO.Request loginInfo) {
        return authenticateAndIssueToken(loginInfo);
    }

    @Override
    public JwtProvider.Response webRefresh(HttpServletRequest request) {
        String refreshToken = extractBearerToken(request.getHeader("Refresh-Token"));
        return jwtProvider.renewAccessToken(refreshToken);
    }

    @Override
    public JwtProvider.Response appRefresh(HttpServletRequest request) {
        String refreshToken = extractBearerToken(request.getHeader("Refresh-Token"));
        return jwtProvider.renewAccessToken(refreshToken);
    }

    private JwtProvider.Response authenticateAndIssueToken(LoginDTO.Request loginInfo) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(
                            loginInfo.getLoginId(),
                            loginInfo.getPassword()
                    )
            );
        } catch (BadCredentialsException exception) {
            return JwtProvider.Response.builder()
                    .result(false)
                    .message("Fail Login")
                    .loginId(loginInfo.getLoginId())
                    .build();
        }

        CustomUserDetails loginMember = (CustomUserDetails) authentication.getPrincipal();

        LoginDTO.Member verifyMember = LoginDTO.Member.builder()
                .loginId(loginMember.getLoginId())
                .status(loginMember.getStatus().name())
                .build();
        JwtProvider.Response jwtResponse = jwtProvider.renewLoginToken(verifyMember);

        return jwtResponse;
    }

    private String extractBearerToken(String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            return null;
        }

        return header.substring(7);
    }
}
