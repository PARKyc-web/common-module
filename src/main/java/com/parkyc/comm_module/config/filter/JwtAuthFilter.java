package com.parkyc.comm_module.config.filter;


import com.parkyc.comm_module.common.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    private static final List<RequestMatcher> WHITE_LIST = List.of(
            PathPatternRequestMatcher.withDefaults().matcher("/page/**"),
            PathPatternRequestMatcher.withDefaults().matcher("/css/**"),
            PathPatternRequestMatcher.withDefaults().matcher("/api/v1/lgn/login"),
            PathPatternRequestMatcher.withDefaults().matcher("/api/v1/member/sign-up"),
            PathPatternRequestMatcher.withDefaults().matcher("/api/v1/member/check-dup-id")
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return WHITE_LIST.stream().anyMatch(matcher -> matcher.matches(request));
    }

    /**
     *  JWT Token 검증 및 UserDetails 세팅
     *  */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authorization header missing or invalid");
            return;
        }

        String token = header.substring(7);

        boolean isValid = jwtProvider.isValidToken(token);
        if(isValid){

        }

        filterChain.doFilter(request, response);
    }
}
