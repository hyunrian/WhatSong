package com.hyunrian.project.service;

import com.hyunrian.project.domain.enums.SearchType;
import com.hyunrian.project.domain.enums.SpotifyRequestType;
import com.hyunrian.project.dto.MusicSearchCondition;
import com.hyunrian.project.dto.SpotifyAlbum;
import com.hyunrian.project.dto.SpotifyArtist;
import com.hyunrian.project.dto.SpotifyMusic;
import com.hyunrian.project.utils.SpotifyUtils;
import org.apache.hc.core5.http.ParseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class MusicServiceTest {

    @Autowired
    private MusicService musicService;

//    @Test
//    void findSpotifyBySearching() throws IOException, ParseException, SpotifyWebApiException, InterruptedException {
//        List<SpotifyMusic> list = getSearchResult();
//
//        assertThat(list.size()).isGreaterThan(0);
//        assertThat(list.get(0).getImageUrl()).isNotNull();
//        assertThat(list.get(0).getReleaseDate()).isNotNull();
//        assertThat(list.get(0).getTrackName()).isNotNull();
//        assertThat(list.get(0).getTrackId()).isNotNull();
//        assertThat(list.get(0).getArtistList()).isNotNull();
//        assertThat(list.get(0).getAlbum()).isNotNull();
//    }
//
//    @Test
//    void getArtistInfo() throws IOException, ParseException, SpotifyWebApiException, InterruptedException {
//        List<SpotifyMusic> list = getSearchResult();
//        String artistId = list.get(0).getArtistList().get(0).getId();
//
//        SpotifyArtist artistInfo = musicService.getArtistInfo(artistId);
//
//        assertThat(artistInfo.getId()).isEqualTo(artistId);
//        assertThat(artistInfo.getImageUrl()).isNotNull();
//        assertThat(artistInfo.getName()).isNotNull();
//        assertThat(artistInfo.getGenreList()).isNotNull();
//    }
//
//    @Test
//    void getAlbumInfo() throws IOException, ParseException, SpotifyWebApiException, InterruptedException {
//        List<SpotifyMusic> list = getSearchResult();
//        String albumId = list.get(0).getAlbum().getId();
//
//        SpotifyAlbum album = musicService.getAlbumInfo(albumId, getAccessToken());
//
//        assertThat(album.getId()).isEqualTo(albumId);
//        assertThat(album.getArtistList()).isNotNull();
//        assertThat(album.getImageUrl()).isNotNull();
//        assertThat(album.getReleaseDate()).isNotNull();
//        assertThat(album.getName()).isNotNull();
//        assertThat(album.getTrackList()).isNotNull();
//        assertThat(album.getArtistList()).isNotNull();
//    }
//
//    String getAccessToken() throws IOException, ParseException, SpotifyWebApiException {
//        SpotifyUtils spotifyUtils = new SpotifyUtils();
//        return spotifyUtils.getAccessToken();
//    }
//
//    List<SpotifyMusic> getSearchResult() throws IOException, ParseException, SpotifyWebApiException, InterruptedException {
//        MusicSearchCondition condition = new MusicSearchCondition();
//        condition.setSearchType(SearchType.TRACK);
//        condition.setKeyword("runaway galantis");
//        return musicService.findSpotifyBySearching(SpotifyRequestType.SEARCH, condition, getAccessToken());
//    }

}