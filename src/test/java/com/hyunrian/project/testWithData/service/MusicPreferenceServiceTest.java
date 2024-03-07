package com.hyunrian.project.testWithData.service;

import com.hyunrian.project.domain.Member;
import com.hyunrian.project.domain.MusicPreference;
import com.hyunrian.project.domain.enums.member.Gender;
import com.hyunrian.project.domain.enums.music.SearchType;
import com.hyunrian.project.dto.member.MemberJoinDto;
import com.hyunrian.project.dto.music.MusicSearchCondition;
import com.hyunrian.project.dto.music.SpotifySearchTrack;
import com.hyunrian.project.service.MemberService;
import com.hyunrian.project.service.MusicPreferenceService;
import com.hyunrian.project.service.SpotifyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:tcp://localhost/~/projectTest",
        "spring.datasource.username=sa",
        "spring.jpa.hibernate.ddl-auto:update"
})
class MusicPreferenceServiceTest {

    @Autowired
    MusicPreferenceService preferenceService;
    @Autowired
    MemberService memberService;
    @Autowired
    SpotifyService spotifyService;

    @Test
    void 조회쿼리확인() {
        Member member = memberService.findById(1L).orElseThrow();
        log.info("====조회 시작====");
        List<MusicPreference> topPreference = preferenceService.getTopPreference(member);
        log.info("====조회 종료====");

        log.info("musicPreference={}", topPreference);
        Assertions.assertThat(topPreference.size()).isEqualTo(5);
    }

    @Test
    @Transactional
    @Rollback(value = false)
    void savePreferences() throws IOException, ParseException, InterruptedException, SpotifyWebApiException {
        //음악 검색
        MusicSearchCondition condition = new MusicSearchCondition();
        condition.setSearchType(SearchType.TRACK);
        condition.setKeyword("아이유");
        List<SpotifySearchTrack> tracks = spotifyService.getSearchResult(condition, 1);

        log.info("tracks={}", tracks);

        //트랙 좋아요 -> MusicPreference 저장
        Member member = memberService.findById(1L).orElseThrow();
        for (int i = 0; i < 10; i++) {
            String trackId = tracks.get(i).getTrackId();
            String artistId = tracks.get(i).getArtistList().get(0).getId();

            List<String> genreList = spotifyService.getArtistInfo(artistId, 1).getGenreList();
            StringBuilder sb = new StringBuilder();
            for (String s : genreList) {
                sb.append(s).append(" ");
            }
            String genre = sb.toString();

            preferenceService.savePreference(member, genre, trackId);
        }
    }
}