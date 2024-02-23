package com.hyunrian.project.service;

import com.hyunrian.project.domain.*;
import com.hyunrian.project.dto.*;
import com.hyunrian.project.repository.*;
import com.hyunrian.project.utils.SpotifyUtils;
import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MusicService {

    private final MusicRepository musicRepository;
    private final MusicQueryRepository musicQueryRepository;
    private final MemberRepository memberRepository;

    public Music save(Music music) {
        return musicRepository.save(music);
    }

    public void updateLikeCount(Long musicId, boolean isLiked) {
        Music music = musicRepository.findById(musicId).orElseThrow();
        music.updateLikeCount(isLiked);
    }

    public Music findById(Long musicId) {
        return musicRepository.findById(musicId).orElseThrow();
    }

    /**
     * 검색
     */
    public List getSearchResult(MusicSearchCondition condition)
            throws IOException, ParseException, SpotifyWebApiException, InterruptedException {

        return SpotifyUtils.getMusicList(condition);
    }

    /**
     * 아티스트 정보
     */
    public SpotifyArtist getArtistInfo(String artistId) throws IOException, ParseException, SpotifyWebApiException, InterruptedException {
        return SpotifyUtils.getArtistDetail(artistId);
    }

    /**
     * 트랙 정보
     */
    public SpotifyMusic getMusicDetail(String trackId) throws IOException, ParseException, InterruptedException, SpotifyWebApiException {
        return SpotifyUtils.getMusicDetail(trackId);
    }

    /**
     * 앨범 정보
     */
    public SpotifyAlbum getAlbumInfo(String albumId, String accessToken) throws IOException, ParseException, SpotifyWebApiException {
        return null;
    }

}
