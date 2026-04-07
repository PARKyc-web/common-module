package com.parkyc.comm_module.security;

import com.parkyc.comm_module.member.domain.entity.Member;
import com.parkyc.comm_module.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        Member member = memberRepository.findMemberByLoginId(loginId);
        if (member == null) {
            throw new UsernameNotFoundException("Member not found: " + loginId);
        }

        return new CustomUserDetails(member);
    }

}
