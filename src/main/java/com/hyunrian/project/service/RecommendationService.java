package com.hyunrian.project.service;

import com.hyunrian.project.domain.Member;
import com.hyunrian.project.domain.MusicPreference;
import com.hyunrian.project.dto.music.SpotifySearchTrack;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class RecommendationService {

    private final MemberService memberService;
    private final MusicPreferenceService musicPreferenceService;
    private final SpotifyService spotifyService;

    public void recommendMusic(String nickname) throws IOException, ParseException, InterruptedException, SpotifyWebApiException {
        Member member = memberService.findByNickname(nickname).orElseThrow();
        List<MusicPreference> topPreference = musicPreferenceService.getTopPreference(member);

        StringBuilder sb = new StringBuilder();
        for (MusicPreference pref : topPreference) {
            sb.append(pref.getTrackId()+",");
        }
        sb.setLength(sb.length() - 1);
        log.info("sb={}", sb);
        List<SpotifySearchTrack> recommendation = spotifyService.getRecommendation(sb.toString());
        for (SpotifySearchTrack track : recommendation) {
            log.info("trackName={}", track.getTrackName());
        }
    }
}
