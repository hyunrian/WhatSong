package com.hyunrian.project.handler;

import com.hyunrian.project.domain.enums.LoginConstant;
import com.hyunrian.project.domain.enums.MemberRole;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
@Slf4j
public class CustomAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final RequestCache requestCache = new HttpSessionRequestCache();
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        clearSession(request);

        SavedRequest savedRequest = requestCache.getRequest(request, response);

         /**
         * prevPage가 존재하는 경우 == 사용자가 직접 /login 경로로 로그인 요청
         * 기존 Session의 prevPage attribute 제거
         */
        String prevPage = (String) request.getSession().getAttribute(LoginConstant.PREV_PAGE);
        if (prevPage != null) {
            log.info("Trying login - prevPage={}", prevPage);
            request.getSession().removeAttribute(LoginConstant.PREV_PAGE);
        }

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        //기본 URL
        String url = "/";

        if (savedRequest != null) {
            url = savedRequest.getRedirectUrl();
            log.info("User Signed in from prevPage={}", url);
        } else if (prevPage != null && !prevPage.equals("")) {

            /**
             * 시작 페이지에서 로그인으로 넘어온 경우
             *  - 기본 URL 유지
             *  - 이메일 인증을 완료하지 않은 사용자인 경우 인증메일 발송 페이지로 이동
             */
            if (prevPage.contains("/join")) {
                if (authorities.contains(new SimpleGrantedAuthority(MemberRole.UNAUTH.getValue()))) {
                    log.info("UnAuthUser signed in from Join Page");
                    url = "/auth";
                }
            } else if (!prevPage.contains("/join")) {
                url = prevPage;
            }
        }

        redirectStrategy.sendRedirect(request, response, url);
    }

    //로그인 실패 후 성공 시 남아있는 에러 세션 제거
    private void clearSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}
