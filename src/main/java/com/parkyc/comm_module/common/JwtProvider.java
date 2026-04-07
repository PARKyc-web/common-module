package com.parkyc.comm_module.common;

import com.parkyc.comm_module.login.dto.LoginDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider {

    /** JWT 결과를 담아서 리턴할 DTO */
    @Data
    @Builder
    public static class Response{
        private boolean result;
        private String message;
        private String loginId;
        private String accessToken;
        private String refreshToken;
    }

    private final SecretKey secretKey;
    private final Long accessExpire;
    private final Long refreshExpire;

    public JwtProvider(@Value("${keys.jjwt}") String keys,
                       @Value("${jwt.expire.access}") Long accessExpire,
                       @Value("${jwt.expire.refresh}") Long refreshExpire){
        this.secretKey = Keys.hmacShaKeyFor(keys.getBytes(StandardCharsets.UTF_8));
        this.accessExpire = accessExpire;
        this.refreshExpire = refreshExpire;
    }

    public Claims verifyToken(String token){
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public JwtProvider.Response renewAccessToken(String refreshToken){
        if (refreshToken == null || refreshToken.isBlank()) {
            return Response.builder()
                    .result(false)
                    .message("Fail to Renew Access Token : Refresh Token IS INVALID")
                    .build();
        }

        try {
            Claims claims = verifyToken(refreshToken);
            String loginId = claims.get("loginId", String.class);
            String status = claims.get("status", String.class);
            Date now = new Date();

            Claims accessClaims = Jwts.claims()
                    .issuer("Common-Module")
                    .issuedAt(now)
                    .add("loginId", loginId)
                    .add("status", status)
                    .build();

            String accessToken = Jwts.builder()
                    .signWith(secretKey)
                    .claims(accessClaims)
                    .expiration(new Date(now.getTime() + accessExpire))
                    .compact();

            return Response.builder()
                    .result(true)
                    .message("Success Renew Access Token!!")
                    .loginId(loginId)
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        } catch (JwtException | IllegalArgumentException exception) {
            return Response.builder()
                    .result(false)
                    .message("Fail to Renew Access Token!!")
                    .build();
        }
    }

    public JwtProvider.Response renewLoginToken(LoginDTO.Member memberInfo){

        Date now = new Date();
        Claims claims = Jwts.claims()
                .issuer("Common-Module")
                .issuedAt(now)
                .add("loginId", memberInfo.getLoginId())
                .add("status", memberInfo.getStatus())
                .build();

        String accessToken = Jwts.builder()
                .signWith(secretKey)
                .claims(claims)
                .expiration(new Date(now.getTime() + accessExpire))
                .compact();

        String refreshToken = Jwts.builder()
                .signWith(secretKey)
                .claims(claims)
                .expiration(new Date(now.getTime() + refreshExpire))
                .compact();

        return Response.builder()
                .result(true)
                .message("Success Renew Login Token!!")
                .loginId(memberInfo.getLoginId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


}
