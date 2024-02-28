package com.hyunrian.project.controller;

import com.hyunrian.project.domain.Member;
import com.hyunrian.project.domain.enums.Gender;
import com.hyunrian.project.domain.enums.LoginConstant;
import com.hyunrian.project.domain.enums.SendingEmailType;
import com.hyunrian.project.dto.MemberJoinDto;
import com.hyunrian.project.dto.MemberLoginDto;
import com.hyunrian.project.service.MemberService;
import com.hyunrian.project.validation.ValidationSequence;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
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
    //이메일 발송 컨트롤러 분리, 로그인 시 미인증회원 별도 처리, 세션 멤버데이터 dto 만들기, 잘못된 접근 페이지 처리
    //소셜 로그인
    @GetMapping("/join")
    public String join(Model model) {
        MemberJoinDto joinDto = new MemberJoinDto();
        joinDto.setBirthYear(1998);
        joinDto.setGender(Gender.FEMALE);
        model.addAttribute("member", joinDto);
        return "member/join";
    }

    @PostMapping("/join")
    public String join(@Validated(ValidationSequence.class)
                           @ModelAttribute(name = "member") MemberJoinDto memberJoinDto,
                       BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) throws MessagingException, UnsupportedEncodingException {

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
        redirectAttributes.addFlashAttribute("email", memberJoinDto.getEmail());
        return "redirect:/joined";
    }

    @GetMapping("/joined")
    public String joinCompleted(@ModelAttribute("email") String email) {
        return "member/emailAuth";
    }

    @GetMapping("/verify")
    public String verifyEmail(HttpSession session, @RequestParam String token) {
        try {
            Member member = memberService.updateStateByToken(token);
            session.setAttribute(LoginConstant.LOGIN_MEMBER, member); //세션에 담을 값을 정해야 함(id만 or 멤버객체)
        } catch (Exception e) {
            // 잘못된 접근입니다 페이지
        }
        return "redirect:/verified";
    }

    @GetMapping("/verified")
    public String verified(HttpSession session, Model model) {
        Member member = (Member) session.getAttribute(LoginConstant.LOGIN_MEMBER);
        model.addAttribute("nickname", member.getNickname());
        return "member/authenticated";
    }

    @ResponseBody
    @PostMapping("/checkDup")
    public boolean checkEmailDuplicate(String data, String name) throws BadRequestException {
        boolean isExisted = memberService.checkDuplicate(data, name);
        return isExisted;
    }

    @GetMapping("/login")
    public String login(MemberLoginDto loginDto, Model model) {
        model.addAttribute("member", loginDto);
        return "member/login";
    }

    @PostMapping("/login")
    public String login(MemberLoginDto loginDto, HttpSession session) {
        session.setAttribute("loginMember", loginDto.getEmail());
        return "redirect:/music";
    }

    @GetMapping("/forgotPw")
    public String forgotPassword(Model model) {
        model.addAttribute("email", new String());
        return "member/newPassword";
    }

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

    @GetMapping("/newPw")
    public String newPwPage(@ModelAttribute("email") String email) {
        return "member/sendingMailForPw";
    }

    @ResponseBody
    @PostMapping("/resend")
    public boolean resendMail(String mailType, String email) throws MessagingException, UnsupportedEncodingException {
        if (mailType.equals("JOIN")) memberService.sendEmail(email, SendingEmailType.JOIN);
        else if (mailType.equals("NEW_PW")) memberService.sendEmail(email, SendingEmailType.NEW_PW);

        return true;
    }

}
