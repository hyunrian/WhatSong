package com.hyunrian.project.service;

import com.hyunrian.project.domain.Album;
import com.hyunrian.project.domain.Member;
import com.hyunrian.project.domain.Music;
import com.hyunrian.project.domain.MusicInAlbum;
import com.hyunrian.project.domain.enums.member.Gender;
import com.hyunrian.project.domain.enums.music.SearchType;
import com.hyunrian.project.dto.music.ArtistDto;
import com.hyunrian.project.dto.member.MemberJoinDto;
import com.hyunrian.project.dto.music.MusicSearchCondition;
import com.hyunrian.project.dto.music.SpotifySearchTrack;
import com.hyunrian.project.repository.MusicRepository;
import com.hyunrian.project.utils.SpotifyUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
class AlbumServiceTest {

    @Autowired AlbumService albumService;
    @Autowired MemberService memberService;
    @Autowired MusicRepository musicRepository;

    @Test
    void getAlbumList() {
        Member member = getSavedMember();
        for (int i = 0; i < 5; i++) {
            albumService.createAlbum("album" + i, member.getNickname());
        }

        List<Album> albumList = albumService.findByMemberId(member.getId());

        assertThat(albumList.size()).isEqualTo(5);
    }

    @Transactional
    @Test
    void addMusicToAlbum() throws IOException, ParseException, InterruptedException, SpotifyWebApiException {
        Member member = getSavedMember();
        Album album = albumService.createAlbum("myAlbum", member.getNickname());

        Music music1 = getMusic(0);
        Music music2 = getMusic(1);
        albumService.addMusicToAlbum(album.getId(), music1);
        albumService.addMusicToAlbum(album.getId(), music2);

        List<MusicInAlbum> musicList = albumService.findMusicListByAlbumId(album.getId());
        log.info("album.getId()={}", album.getId());
        log.info("musicList={}", album.getMusicInAlbumList());

        assertThat(musicList.size()).isEqualTo(2);
    }

    private Music getMusic(int order) throws IOException, ParseException, SpotifyWebApiException, InterruptedException {
        MusicSearchCondition condition = new MusicSearchCondition();
        condition.setSearchType(SearchType.TRACK);
        condition.setKeyword("galantis");

        SpotifySearchTrack track = (SpotifySearchTrack) SpotifyUtils.getMusicList(condition, 1).get(order);
        ArtistDto artistDto = track.getArtistList().get(0);

        log.info("trackId={}", track.getTrackId());
        log.info("artistId={}", artistDto.getId());

        Music music = new Music(track.getTrackId(), track.getTrackName(),
                artistDto.getId(), artistDto.getName(), track.getImageUrl());
        return music;
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