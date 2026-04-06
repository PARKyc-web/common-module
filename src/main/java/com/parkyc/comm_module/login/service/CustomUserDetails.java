package com.parkyc.comm_module.login.service;

import com.parkyc.comm_module.common.code.MemberStatus;
import com.parkyc.comm_module.member.domain.entity.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class CustomUserDetails implements UserDetails {

    private final Long memberId;
    private final String loginId;
    private final String password;
    private final MemberStatus status;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Member member) {
        this.memberId = member.getMemberId();
        this.loginId = member.getLoginId();
        this.password = member.getPassword();
        this.status = member.getStatus();
        this.authorities = AuthorityUtils.createAuthorityList("ROLE_USER");
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return loginId;
    }
}
