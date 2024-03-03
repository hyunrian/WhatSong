package com.hyunrian.project.controller;

import com.hyunrian.project.domain.Member;
import com.hyunrian.project.domain.enums.member.Gender;
import com.hyunrian.project.domain.enums.member.LoginConstant;
import com.hyunrian.project.domain.enums.member.MemberStatus;
import com.hyunrian.project.domain.enums.member.SendingEmailType;
import com.hyunrian.project.dto.member.MemberJoinDto;
import com.hyunrian.project.service.MemberService;
import com.hyunrian.project.validation.ValidationSequence;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    //잘못된 접근 페이지 처리
    //메일 인증 후 시작하기 페이지 버튼 css 확인
    //소셜 로그인

    /**
     * 회원가입 화면
     */
    @GetMapping("/join")
    public String join(Model model) {
        MemberJoinDto joinDto = new MemberJoinDto();
        joinDto.setBirthYear(1998);
        joinDto.setGender(Gender.FEMALE);
        model.addAttribute("member", joinDto);
        return "member/join";
    }

    /**
     * 회원가입 처리
     */
    @PostMapping("/join")
    public String join(@Validated(ValidationSequence.class)
                       @ModelAttribute(name = "member") MemberJoinDto memberJoinDto,
                       BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {

        boolean isDupEmail = memberService.checkEmailDup(memberJoinDto);
        boolean isDupNickname = memberService.checkNicknameDup(memberJoinDto);

        if (isDupEmail || isDupNickname) {
            String field = "";
            String message = "";
            String rejectedValue = "";

            if (isDupEmail) {
                field = "email";
                rejectedValue = memberJoinDto.getEmail();
                message = "이미 등록되어 있는 이메일입니다.";
            } else if (isDupNickname) {
                field = "nickname";
                rejectedValue = memberJoinDto.getNickname();
                message = "이미 사용중인 닉네임입니다.";
            }
            bindingResult.addError(new FieldError("member", field,
                    rejectedValue, false, null, null, message));
        }

        if (bindingResult.hasErrors()) {
            log.info("Login Error {}", bindingResult);
            return "member/join";
        }

        memberService.save(memberJoinDto);
        memberService.sendEmail(memberJoinDto.getEmail(), SendingEmailType.JOIN);

        request.getSession().setAttribute("email", memberJoinDto.getEmail());

        return "redirect:/joined";
    }

    /**
     * 이메일 인증 화면
     */
    @GetMapping("/auth")
    public String joinCompleted(HttpServletRequest request, Model model) throws MessagingException, UnsupportedEncodingException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = "";

        /**
         * 회원가입 후 바로 메일인증을 하는 사용자인 경우 Session에 담은 값을 가져옴
         * 인증을 하지 않은 사용자가 로그인을 한 경우 Security Session에 있는 값을 가져옴
         */
        if (authentication.getName() != null) {
            email = authentication.getName();

        } else {
            email = (String) request.getSession().getAttribute("email");
        }

        model.addAttribute("email", email);

        //로그인한 사용자가 미인증 사용자인 경우 인증메일 발송
        Member loginMember = memberService.findByEmail(email).orElseThrow();
        if (loginMember.getStatus().equals(MemberStatus.UNAUTH)) {
            memberService.sendEmail(email, SendingEmailType.JOIN);
        }

        return "member/emailAuth";
    }

    /**
     * 인증 메일 발송(MemberService에서 사용)
     */
    @GetMapping("/verify")
    public String verifyEmail(HttpSession session, @RequestParam String token) {
        try {
            Member member = memberService.updateStateByToken(token);
            session.setAttribute(LoginConstant.LOGIN_MEMBER, member.getNickname());
        } catch (Exception e) {
            // 토큰 값이 없는 경우 잘못된 접근입니다 페이지
        }
        return "redirect:/verified";
    }

    /**
     * 메일 인증 링크 클릭 시 화면
     */
    @GetMapping("/verified")
    public String verified(HttpServletRequest request, Model model) throws BadRequestException {
        String nickname = (String) request.getSession().getAttribute(LoginConstant.LOGIN_MEMBER);

        if (nickname != null) {
            log.info("Verified page accessed by user nickname={}", nickname);
            Member member = memberService.findByNickname(nickname).orElseThrow();
            if (member.getStatus().equals(MemberStatus.UNAUTH)) {
                throw new BadRequestException("잘못된 접근");
            }
        } else {
            throw new BadRequestException("잘못된 접근");
        }

        model.addAttribute("nickname", nickname);

        String prevPage = (String) request.getSession().getAttribute(LoginConstant.PREV_PAGE);
        if (prevPage != null) {
            model.addAttribute("prevPage", prevPage);
        }

        return "member/authenticated";
    }

    /**
     * 메일 재발송
     */
    @ResponseBody
    @PostMapping("/resend")
    public boolean resendMail(String mailType, String email) throws MessagingException, UnsupportedEncodingException {
        if (mailType.equals("JOIN")) memberService.sendEmail(email, SendingEmailType.JOIN);
        else if (mailType.equals("NEW_PW")) memberService.sendEmail(email, SendingEmailType.NEW_PW);

        return true;
    }

    /**
     * 중복확인
     */
    @ResponseBody
    @PostMapping("/checkDup")
    public boolean checkEmailDuplicate(String data, String name) throws BadRequestException {
        log.info("data={}, name={}", data, name);
        boolean isExisted = memberService.checkDuplicate(data, name);
        return isExisted;
    }

    /**
     * 로그인 화면
     */
    @GetMapping("/login")
    public String login(HttpServletRequest request) {

        String uri = request.getHeader("Referer");
        log.info("LOGIN PAGE requested - prevPage={}", uri);
        if (uri != null && !uri.contains("/login")) {
            request.getSession().setAttribute(LoginConstant.PREV_PAGE, uri);
        }
        return "member/login";
    }

    /**
     * 비밀번호 찾기 화면
     */
    @GetMapping("/forgotPw")
    public String forgotPassword(Model model) {
        model.addAttribute("email", new String());
        return "member/newPassword";
    }

    /**
     * 임시 비밀번호 발급 메일 발송
     */
    @PostMapping("/forgotPw")
    public String forgotPassword(String email, Model model, RedirectAttributes redirectAttributes) throws MessagingException, UnsupportedEncodingException {
        Member member = memberService.findByEmail(email).orElse(null);

        if (member == null) {
            model.addAttribute("result", "null");
            model.addAttribute("email", email);
            return "member/newPassword";
        }

        memberService.sendEmail(email, SendingEmailType.NEW_PW);
        redirectAttributes.addFlashAttribute("email", email);

        return "redirect:/newPw";
    }

    /**
     * 임시 비밀번호 발급 완료 화면
     */
    @GetMapping("/newPw")
    public String newPwPage(@ModelAttribute("email") String email) {
        return "member/sendingMailForPw";
    }

}
