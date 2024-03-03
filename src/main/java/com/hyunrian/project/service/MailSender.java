package com.hyunrian.project.service;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
@RequiredArgsConstructor
public class MailSender {

    private final JavaMailSender mailSender;
    private final String ADMIN_EMAIL = "sweethyunii@naver.com";
    private final String ADMIN_NAME = "WhatSong_Admin";

    public void sendAuthMail(String email, String token) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = mailSender.createMimeMessage();
        message.addRecipients(Message.RecipientType.TO, email);

        StringBuilder sb = new StringBuilder();

        message.setSubject("WhatSong 회원가입 이메일 인증");

        String request = "http://localhost:8080/verify?token=" + token;

        sb.append("<div>");
        sb.append("<h1>안녕하세요, WhatSong입니다.</h1>");
        sb.append("<br><br>");
        sb.append("<p>아래 링크를 클릭하시면 이메일 인증이 완료됩니다.</p><br>");
        sb.append("<a href='" + request + "' class='button'>인증하기</a>");
        sb.append("</div>");

        message.setText(sb.toString(), "utf-8", "html");
        message.setFrom(new InternetAddress(ADMIN_EMAIL, ADMIN_NAME));
        mailSender.send(message);
    }

    public void sendNewPwMail(String email, String tempPw) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        message.addRecipients(Message.RecipientType.TO, email);

        StringBuilder sb = new StringBuilder();

        message.setSubject("WhatSong 임시 비밀번호 안내");

        sb.append("<div>");
        sb.append("<h1>안녕하세요, WhatSong입니다.</h1>");
        sb.append("<br><br>");
        sb.append("<p>발급된 임시 비밀번호는 다음과 같습니다.</p><br>");
        sb.append("<p><b>" + tempPw + "</b></p><br>");
        sb.append("<p>보안을 위해 로그인 후 비밀번호를 변경해주시기 바랍니다.</p><br>");
        sb.append("</div>");

        message.setText(sb.toString(), "utf-8", "html");
        message.setFrom(new InternetAddress(ADMIN_EMAIL, ADMIN_NAME));
        mailSender.send(message);
    }
}
