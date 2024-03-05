package com.hyunrian.project.service;

import com.hyunrian.project.domain.Member;
import com.hyunrian.project.domain.MusicPreference;
import com.hyunrian.project.domain.enums.member.Gender;
import com.hyunrian.project.dto.member.MemberJoinDto;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class MusicPreferenceServiceTest {

    @Autowired
    MusicPreferenceService preferenceService;
    @Autowired
    MemberService memberService;

    @Test
    void getList() {
        Member member = getSavedMember();
        savePreferences(member);

        List<MusicPreference> topPreference = preferenceService.getTopPreference(member);

        log.info("musicPreference={}", topPreference);
        Assertions.assertThat(topPreference.size()).isEqualTo(5);
    }

    private void savePreferences(Member member) {
        preferenceService.savePreference(member, "K-POP, ACOUSTIC", "tempTrackId1");
        preferenceService.savePreference(member, "POP", "tempTrackId2");
        preferenceService.savePreference(member, "K-POP, ACOUSTIC", "tempTrackId3");
        preferenceService.savePreference(member, "POP", "tempTrackId4");
        preferenceService.savePreference(member, "ROCK", "tempTrackId5");
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