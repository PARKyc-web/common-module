package com.parkyc.comm_module.common;

import io.jsonwebtoken.Claims;
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

    public boolean isValidToken(String token){
        Claims claims = verifyToken(token);

        Date now = new Date();
        Date expire = claims.getExpiration();

        return now.before(expire);
    }

    public Claims verifyToken(String token){
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims;
    }

    public JwtProvider.Response renewAccessToken(String refreshToken){


        return JwtProvider.Response.builder().build();
    }

    public JwtProvider.Response renewLoginToken(String username){
        return null;
    }


}
