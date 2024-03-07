package com.hyunrian.project.service;

import com.hyunrian.project.domain.Album;
import com.hyunrian.project.domain.Member;
import com.hyunrian.project.domain.MusicPreference;
import com.hyunrian.project.domain.enums.member.Gender;
import com.hyunrian.project.dto.member.MemberJoinDto;
import com.hyunrian.project.dto.member.MemberUpdateDto;
import com.hyunrian.project.repository.AlbumRepository;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
//@Transactional
@Slf4j
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired AlbumService albumService;

    @BeforeEach
    void setUpForAlbumTest() {

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

    @Test
    void setMember() {
        MemberJoinDto joinDto = new MemberJoinDto();
        joinDto.setNickname("tester");
        joinDto.setPassword("123");
        joinDto.setBirthYear(2000);
        joinDto.setGender(Gender.FEMALE);
        joinDto.setEmail("aaa");

        memberService.save(joinDto);
    }

    @Test
    void setAlbumData() {
        for (int i = 0; i < 5; i++) {
            albumService.createAlbum("albumName" + i, "tester");
        }
    }

    @Test
    void find_쿼리확인() {
        Member member = memberService.findById(1L).orElseThrow();
        List<Album> memberAlbum = memberService.findMemberAlbum(member);
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