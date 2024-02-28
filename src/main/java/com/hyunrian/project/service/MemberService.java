package com.hyunrian.project.service;

import com.hyunrian.project.domain.Member;
import com.hyunrian.project.domain.enums.SendingEmailType;
import com.hyunrian.project.dto.MemberJoinDto;
import com.hyunrian.project.dto.MemberUpdateDto;
import com.hyunrian.project.exception.AlreadyRegisteredException;
import com.hyunrian.project.repository.MemberRepository;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.UniqueConstraint;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final JavaMailSender mailSender;

    public Member save(MemberJoinDto joinDto) throws MessagingException, UnsupportedEncodingException {
//        joinDto.setToken(UUID.randomUUID().toString());
        Member member = new Member().getJoinMember(joinDto);
        Member joinedMember = memberRepository.save(member);
        sendEmail(joinedMember.getEmail(), SendingEmailType.JOIN);

        return joinedMember;
    }

    public void sendEmail(String email, SendingEmailType emailType) throws MessagingException, UnsupportedEncodingException {

        Member member = findByEmail(email).orElseThrow();
        String token = UUID.randomUUID().toString();
        member.updateToken(token);

        MimeMessage message = mailSender.createMimeMessage();
        message.addRecipients(Message.RecipientType.TO, email);

        StringBuilder sb = new StringBuilder();

        switch (emailType) {
            case JOIN :
                message.setSubject("WhatSong 회원가입 이메일 인증");

                String requestAuth = "http://localhost:8080/verify?token=" + token;

                sb.append("<div>");
                sb.append("<h1>안녕하세요, WhatSong입니다.</h1>");
                sb.append("<br><br>");
                sb.append("<p>아래 링크를 클릭하시면 이메일 인증이 완료됩니다.</p><br>");
                sb.append("<a href='" + requestAuth + "' class='button'>인증하기</a>");
                sb.append("</div>");
                break;
            case NEW_PW :

                String tempPw = UUID.randomUUID().toString().substring(0, 8);
                member.changePwToTemp(tempPw);

                message.setSubject("WhatSong 임시 비밀번호 안내");

                sb.append("<div>");
                sb.append("<h1>안녕하세요, WhatSong입니다.</h1>");
                sb.append("<br><br>");
                sb.append("<p>발급된 임시 비밀번호는 다음과 같습니다.</p><br>");
                sb.append("<p><b>" + tempPw + "</b></p><br>");
                sb.append("<p>보안을 위해 로그인 후 비밀번호를 변경해주시기 바랍니다.</p><br>");
                sb.append("</div>");
        }

        message.setText(sb.toString(), "utf-8", "html");
        message.setFrom(new InternetAddress("sweethyunii@naver.com", "WhatSong_Admin"));
        mailSender.send(message);
    }

    public Member updateStateByToken(String token) {
        Member member = memberRepository.findByToken(token).orElseThrow();
        if (member != null) {
            member.updateStatus();
        }
        return member;
    }

    public boolean checkEmailDup(MemberJoinDto joinDto) {
        return memberRepository.existsByEmail(joinDto.getEmail());
    }

    public boolean checkNicknameDup(MemberJoinDto joinDto) {
        return memberRepository.existsByNickname(joinDto.getNickname());
    }

    public void update(Long id, MemberUpdateDto memberUpdateDto) {
        Member member = memberRepository.findById(id).orElseThrow();
        member.updateMember(memberUpdateDto.getNickname(), memberUpdateDto.getPassword(), memberUpdateDto.getImagePath());
    }

    public boolean checkDuplicate(String data, String name) throws BadRequestException {
        switch (name) {
            case "nickname":
                return memberRepository.existsByNickname(data);
            case "email":
                return memberRepository.existsByEmail(data);
        }
        throw new BadRequestException("잘못된 요청");
    }

    @Transactional(readOnly = true)
    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public void delete(Long id) {
        Member member = memberRepository.findById(id).orElseThrow();
        memberRepository.delete(member);
    }


}
