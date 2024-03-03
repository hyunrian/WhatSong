package com.hyunrian.project.controller;

import com.hyunrian.project.domain.enums.member.LoginConstant;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class HomeController {

    @GetMapping("/")
    public String home(HttpServletRequest request, Model model) {
//        HttpSession session = request.getSession();
//        String loginMember = (String) session.getAttribute(LoginConstant.LOGIN_MEMBER);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginMember = authentication.getName();

        if (loginMember.equals(LoginConstant.ANONYMOUS)) {
            return "index";
        }

        log.info("Security Session user={}", loginMember);
        model.addAttribute("user", loginMember);
        return "main/search/search";

    }
}
