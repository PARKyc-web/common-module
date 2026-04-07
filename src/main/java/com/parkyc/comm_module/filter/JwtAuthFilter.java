package com.parkyc.comm_module.filter;


import com.parkyc.comm_module.common.JwtProvider;
import com.parkyc.comm_module.security.CustomUserDetails;
import com.parkyc.comm_module.security.CustomUserDetailsService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final CustomUserDetailsService customUserDetailsService;

    private static final List<RequestMatcher> WHITE_LIST = List.of(
            PathPatternRequestMatcher.withDefaults().matcher("/page/**"),
            PathPatternRequestMatcher.withDefaults().matcher("/css/**"),
            PathPatternRequestMatcher.withDefaults().matcher("/api/v1/lgn/web/login"),
            PathPatternRequestMatcher.withDefaults().matcher("/api/v1/lgn/web/refresh"),
            PathPatternRequestMatcher.withDefaults().matcher("/api/v1/lgn/app/login"),
            PathPatternRequestMatcher.withDefaults().matcher("/api/v1/lgn/app/refresh"),
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
        try {
            Claims claims = jwtProvider.verifyToken(token);
            String loginId = claims.get("loginId", String.class);
            if (loginId == null || loginId.isBlank()) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT token missing loginId");
                return;
            }

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                CustomUserDetails userDetails =
                        (CustomUserDetails) customUserDetailsService.loadUserByUsername(loginId);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception exception) {
            SecurityContextHolder.clearContext();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT token invalid");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
