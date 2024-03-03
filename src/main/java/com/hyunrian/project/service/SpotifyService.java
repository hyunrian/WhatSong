package com.hyunrian.project.service;

import com.hyunrian.project.dto.music.*;
import com.hyunrian.project.utils.SpotifyUtils;
import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SpotifyService {

    /**
     * 검색
     */
    public List getSearchResult(MusicSearchCondition condition, int page)
            throws IOException, ParseException, SpotifyWebApiException, InterruptedException {

        return SpotifyUtils.getMusicList(condition, page);
    }

    /**
     * 아티스트 정보
     */
    public SpotifyArtist getArtistInfo(String artistId, int albumPage) throws IOException, ParseException, SpotifyWebApiException, InterruptedException {
        return SpotifyUtils.getArtistDetail(artistId, albumPage);
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
    public SpotifyAlbum getAlbumInfo(String albumId, int albumPage) throws IOException, ParseException, SpotifyWebApiException, InterruptedException {
        return SpotifyUtils.getAlbumDetail(albumId, albumPage);
    }

    /**
     * 최신앨범
     */
    public List<SpotifyNewRelease> getNewReleaseAlbum(int page) throws IOException, ParseException, InterruptedException, SpotifyWebApiException {
        return SpotifyUtils.getNewReleaseAlbum(page);
    }

}
