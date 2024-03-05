package com.hyunrian.project.repository;

import com.hyunrian.project.domain.Member;
import com.hyunrian.project.dto.member.MemberJoinDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:tcp://localhost/~/projectTest",
        "spring.datasource.username=sa",
        "spring.jpa.hibernate.ddl-auto:create"
})
@Slf4j
public class MemberRepositoryPerformanceTest {

    @Autowired private MemberRepository memberRepository;


    //DB에 테스트용 데이터가 삭제된 경우 실행
    @BeforeEach
    void setUpData() {
        //Member 데이터 삽입
        for (int i = 0; i < 100; i++) {
            MemberJoinDto joinDto = new MemberJoinDto();
            joinDto.setNickname("tester" + i);
            joinDto.setPassword("a1234567");
            joinDto.setEmail("tester" + i + "@mail.com");

            Member joinMember = new Member().getJoinMember(joinDto);
            memberRepository.save(joinMember);
        }

        //Member의 앨범 데이터 삽입
    }


    @Test
    @Rollback(value = false)
    void testReadOnly_findAll() {
        long readOnlyFalseTime = testReadOnlyFalse_findAll();
        long readOnlyTrueTime = testReadOnlyTrue_findAll();
        log.info("readOnly false time={}", readOnlyFalseTime);
        log.info("readOnly true time={}", readOnlyTrueTime);
    }

    @Transactional
    long testReadOnlyFalse_findAll() {
        long startTime = System.currentTimeMillis();
        List<Member> list = memberRepository.findAll();
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

    @Transactional(readOnly = true)
    long testReadOnlyTrue_findAll() {
        long startTime = System.currentTimeMillis();
        List<Member> list = memberRepository.findAll();
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }
}
