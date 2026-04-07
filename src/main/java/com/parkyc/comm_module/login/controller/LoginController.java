package com.parkyc.comm_module.login.controller;

import com.parkyc.comm_module.common.JwtProvider;
import com.parkyc.comm_module.login.dto.LoginDTO;
import com.parkyc.comm_module.login.service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/lgn")
@RestController
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @PostMapping("/web/login")
    public ResponseEntity<LoginDTO.Response> webLogin(@RequestBody LoginDTO.Request loginInfo,
                                                      HttpServletRequest request){
        JwtProvider.Response response = loginService.webLogin(loginInfo);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, createRefreshTokenCookie(response.getRefreshToken(), request).toString())
                .body(toWebResponse(response));
    }

    @PostMapping("/web/refresh")
    public ResponseEntity<LoginDTO.Response> webRefresh(HttpServletRequest request){
        JwtProvider.Response response = loginService.webRefresh(request);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, createRefreshTokenCookie(response.getRefreshToken(), request).toString())
                .body(toWebResponse(response));
    }

    @PostMapping("/app/login")
    public LoginDTO.AppResponse appLogin(@RequestBody LoginDTO.Request loginInfo){
        JwtProvider.Response response = loginService.appLogin(loginInfo);

        return toAppResponse(response);
    }

    @PostMapping("/app/refresh")
    public LoginDTO.AppResponse appRefresh(HttpServletRequest request){
        JwtProvider.Response response = loginService.appRefresh(request);

        return toAppResponse(response);
    }

    private ResponseCookie createRefreshTokenCookie(String refreshToken, HttpServletRequest request) {
        return ResponseCookie.from("refreshToken", refreshToken == null ? "" : refreshToken)
                .httpOnly(true)
                .secure(request.isSecure())
                .path("/")
                .sameSite("Lax")
                .maxAge(60L * 60 * 24 * 30)
                .build();
    }

    private LoginDTO.Response toWebResponse(JwtProvider.Response response) {
        return LoginDTO.Response.builder()
                .result(response.isResult() ? LoginDTO.Code.SUCCESS : LoginDTO.Code.FAIL)
                .loginId(response.getLoginId())
                .accessToken(response.getAccessToken())
                .build();
    }

    private LoginDTO.AppResponse toAppResponse(JwtProvider.Response response) {
        return LoginDTO.AppResponse.builder()
                .result(response.isResult() ? LoginDTO.Code.SUCCESS : LoginDTO.Code.FAIL)
                .loginId(response.getLoginId())
                .accessToken(response.getAccessToken())
                .refreshToken(response.getRefreshToken())
                .build();
    }

}
