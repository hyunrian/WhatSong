package com.hyunrian.project.service;

import com.hyunrian.project.domain.Member;
import com.hyunrian.project.domain.enums.Gender;
import com.hyunrian.project.dto.MemberJoinDto;
import com.hyunrian.project.dto.MemberUpdateDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Test
    void save() {
        MemberJoinDto joinDto = new MemberJoinDto();
        joinDto.setNickname("tester");
        joinDto.setPassword("123");
        joinDto.setAge(20);
        joinDto.setGender(Gender.FEMALE);
        joinDto.setEmail("aaa");

        Member savedMember = memberService.save(joinDto);

        assertThat(savedMember.getNickname()).isEqualTo(joinDto.getNickname());
        assertThat(savedMember.getPassword()).isEqualTo(joinDto.getPassword());
        assertThat(savedMember.getEmail()).isEqualTo(joinDto.getEmail());
        assertThat(savedMember.getAge()).isEqualTo(joinDto.getAge());
        assertThat(savedMember.getGender()).isEqualTo(joinDto.getGender());
    }

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
    void delete() {
        Long id = getSavedMember().getId();
        Member savedMember = memberService.findById(id).orElse(null);
        assertThat(savedMember).isNotNull();

        memberService.delete(id);

        Member deletedMember = memberService.findById(id).orElse(null);
        assertThat(deletedMember).isNull();
    }

    Member getSavedMember() {
        MemberJoinDto joinDto = new MemberJoinDto();
        joinDto.setNickname("tester");
        joinDto.setPassword("123");
        joinDto.setAge(20);
        joinDto.setGender(Gender.FEMALE);
        joinDto.setEmail("aaa");

        return memberService.save(joinDto);
    }
}