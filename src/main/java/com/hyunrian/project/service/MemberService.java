package com.hyunrian.project.service;

import com.hyunrian.project.domain.Album;
import com.hyunrian.project.domain.Member;
import com.hyunrian.project.domain.enums.member.SendingEmailType;
import com.hyunrian.project.dto.member.MemberJoinDto;
import com.hyunrian.project.dto.member.MemberUpdateDto;
import com.hyunrian.project.repository.MemberRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
//@Transactional(readOnly = true)
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSender mailSender;

    @Transactional
    public Member save(MemberJoinDto joinDto) {

        joinDto.setPassword(passwordEncoder.encode(joinDto.getPassword()));

        Member member = new Member().getJoinMember(joinDto);
        return memberRepository.save(member);
    }

    @Transactional
    public void sendEmail(String email, SendingEmailType emailType) throws MessagingException, UnsupportedEncodingException {

        Member member = findByEmail(email).orElseThrow();
        log.info("Sending Mail To User - email={}, emailType={}", email, emailType.toString());

        switch (emailType) {
            case JOIN :
                String token = UUID.randomUUID().toString();
                member.updateToken(token);
                mailSender.sendAuthMail(email, token);
                break;

            case NEW_PW :
                String tempPw = UUID.randomUUID().toString().substring(0, 8);
                member.changePwToTemp(tempPw);
                mailSender.sendNewPwMail(email, tempPw);
        }
    }

    @Transactional
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

    @Transactional
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

    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }

    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public String findNicknameByEmail(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow();
        return member.getNickname();
    }

    public Optional<Member> findByNickname(String nickname) {
        return memberRepository.findByNickname(nickname);
    }

    @Transactional
    public void delete(Long id) {
        Member member = memberRepository.findById(id).orElseThrow();
        memberRepository.delete(member);
    }

}
