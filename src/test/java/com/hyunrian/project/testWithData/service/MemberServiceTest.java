package com.hyunrian.project.testWithData.service;

import com.hyunrian.project.domain.Member;
import com.hyunrian.project.domain.MusicPreference;
import com.hyunrian.project.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Slf4j
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:tcp://localhost/~/projectTest",
        "spring.datasource.username=sa",
        "spring.jpa.hibernate.ddl-auto:update"
})
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Test
    @Transactional
    void musicPreference_조회쿼리확인() {
        List<Member> memberList = memberService.findAll();

        log.info("====조회 시작====");
        for (Member member : memberList) {
            logMusicPreference(member);
        }
        log.info("====조회 종료====");
    }

    void logMusicPreference(Member member) {
        List<MusicPreference> list = member.getMusicPreferenceList();
        log.info("member={}, musicPreference={}", member, list);
    }
}
