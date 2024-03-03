package com.hyunrian.project.service;

import com.hyunrian.project.domain.Member;
import com.hyunrian.project.domain.enums.member.Gender;
import com.hyunrian.project.dto.member.MemberJoinDto;
import com.hyunrian.project.dto.member.MemberUpdateDto;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Test
    void update() {
        Long id = getSavedMember().getId();
        MemberUpdateDto updateDto = new MemberUpdateDto();
        updateDto.setNickname("updateTest");
        updateDto.setPassword("222");

        memberService.update(id, updateDto);
        Member updatedMember = memberService.findById(id).orElse(null);

        assertThat(updatedMember.getNickname()).isEqualTo(updateDto.getNickname());
        assertThat(updatedMember.getPassword()).isEqualTo(updateDto.getPassword());
    }

    @Test
    void delete() throws MessagingException, UnsupportedEncodingException {
        Long id = getSavedMember().getId();
        Member savedMember = memberService.findById(id).orElse(null);
        assertThat(savedMember).isNotNull();

        memberService.delete(id);

        Member deletedMember = memberService.findById(id).orElse(null);
        assertThat(deletedMember).isNull();
    }

    @Test
    void findByEmail() throws MessagingException, UnsupportedEncodingException {
        Member savedMember = getSavedMember();
        String email = savedMember.getEmail();

        Member member = memberService.findByEmail(email).orElse(null);

        assertThat(savedMember.getId()).isEqualTo(member.getId());
    }

    Member getSavedMember() {
        MemberJoinDto joinDto = new MemberJoinDto();
        joinDto.setNickname("tester");
        joinDto.setPassword("123");
        joinDto.setBirthYear(2000);
        joinDto.setGender(Gender.FEMALE);
        joinDto.setEmail("aaa");

        return memberService.save(joinDto);
    }

}