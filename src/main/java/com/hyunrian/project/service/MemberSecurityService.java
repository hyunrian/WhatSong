package com.hyunrian.project.service;

import com.hyunrian.project.domain.Member;
import com.hyunrian.project.domain.enums.LoginConstant;
import com.hyunrian.project.domain.enums.MemberRole;
import com.hyunrian.project.domain.enums.MemberStatus;
import com.hyunrian.project.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberSecurityService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final HttpServletRequest request;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> _member = this.memberRepository.findByEmail(username);
        if (_member.isEmpty()) {
            throw new UsernameNotFoundException("존재하지 않는 사용자입니다.");
        }

        Member member = _member.get();
        MemberStatus status = member.getStatus();

        List<GrantedAuthority> authorities = new ArrayList<>();
        if ("sweethyunii@naver.com".equals(username)) {
            authorities.add(new SimpleGrantedAuthority(MemberRole.ADMIN.getValue()));
        } else if (status.equals(MemberStatus.UNAUTH)) {
            authorities.add(new SimpleGrantedAuthority(MemberRole.UNAUTH.getValue()));
        } else {
            authorities.add(new SimpleGrantedAuthority(MemberRole.USER.getValue()));
        }

//        request.getSession().setAttribute(LoginConstant.LOGIN_MEMBER, username);

        return new User(member.getEmail(), member.getPassword(), authorities);
    }
}
