package com.hyunrian.project.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyunrian.project.domain.Member;
import com.hyunrian.project.domain.Music;
import com.hyunrian.project.domain.enums.BrandName;
import com.hyunrian.project.domain.enums.SearchType;
import com.hyunrian.project.domain.enums.SpotifyRequestType;
import com.hyunrian.project.dto.*;
import com.hyunrian.project.repository.MemberRepository;
import com.hyunrian.project.repository.MusicQueryRepository;
import com.hyunrian.project.repository.MusicRepository;
import com.hyunrian.project.utils.SpotifyUtils;
import lombok.RequiredArgsConstructor;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;
import java.util.List;

import static com.hyunrian.project.domain.enums.FindMusicConstants.*;

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
     * 노래 조회(나의 앨범)
     */
    public List<MusicAndAlbumInfo> findMusicInAlbum(Long memberId, MusicInAlbumDto musicInAlbumDto) {
        Member member = memberRepository.findById(memberId).orElseThrow();
        musicInAlbumDto.setBrandName(member.getBrandName());
        return musicQueryRepository.findAll(musicInAlbumDto);
    }

    /**
     * 노래 조회(API) - 검색
     */
//    public List<MusicSearchDto> findBySearching(MusicSearchCondition musicSearchCondition) throws JsonProcessingException {
//        SearchType searchType = musicSearchCondition.getSearchType();
//        String keyword = musicSearchCondition.getKeyword();
//        BrandName brandName = musicSearchCondition.getBrandName();
//
//        return getMusicList(searchType, keyword, brandName, SEARCH);
//    }

    /**
     * 노래 조회(API) - 최신곡
     */
//    public List<MusicSearchDto> findLatest(BrandName brandName) throws JsonProcessingException {
//        return getMusicList(null, null, brandName, RECENT);
//    }
//    public List<MusicSearchDto> findLatest(BrandName brandName) throws IOException, ParseException, SpotifyWebApiException {
//        List<MusicSearchDto> musicList = getMusicList(null, null, brandName, RECENT);
//        for (MusicSearchDto musicSearchDto : musicList) {
//            String title = musicSearchDto.getTitle();
//            String singer = musicSearchDto.getSinger();
//            String release = musicSearchDto.getRelease();
//
//            String albumImage = getAlbumImage(title, singer);
//            musicSearchDto.setImageUrl(albumImage);
//        }
//        return musicList;
//    }

    /**
     * 노래 조회(Spotify) - 검색
     */
    public List<SpotifyMusic> findSpotifyBySearching(SpotifyRequestType spotifyType, MusicSearchCondition condition, String accessToken)
            throws IOException, ParseException, SpotifyWebApiException, InterruptedException {

        return SpotifyUtils.getMusicList(spotifyType, condition, accessToken);
    }

    /**
     * 아티스트 정보
     */
    public SpotifyArtist getArtistInfo(String artistId, String accessToken) throws IOException, ParseException, SpotifyWebApiException {
        return SpotifyUtils.getArtistInfo(artistId, accessToken);
    }

    /**
     * 앨범 정보
     */
    public SpotifyAlbum getAlbumInfo(String albumId, String accessToken) throws IOException, ParseException, SpotifyWebApiException {
        return SpotifyUtils.getAlbumInfo(albumId, accessToken);
    }

    /*
    private String getAlbumImage(String title, String singer) throws IOException, ParseException, SpotifyWebApiException {
        String keyword = "";

        if (title.contains("(")) {
            int i = title.indexOf("(");
            StringBuilder sb = new StringBuilder(title);
            sb.setLength(i);
            String s = sb.toString().trim();
            keyword = s;
//                System.out.println(s + " " + singer);
        } else {
            keyword = title;
//                System.out.println(title + " " + singer);
        }

        SpotifyMusic spotifyMusic = findSpotifyBySearching(keyword + " " + singer).get(0);

//            System.out.println("spotify = " + spotifyMusic.getTitle() + " & " + spotifyMusic.getSinger());

        String resultTitle = title.replaceAll("\\s", "");
        String spotifyTitle = spotifyMusic.getTrackName().replaceAll("\\s", "");
        if (spotifyTitle.contains("(")) {
            int i = spotifyTitle.indexOf("(");
            StringBuilder sb = new StringBuilder(spotifyTitle);
            sb.setLength(i);
            String s = sb.toString().trim();
            spotifyTitle = s;
        } else if (spotifyTitle.contains("-")) {
            int i = spotifyTitle.indexOf("-");
            StringBuilder sb = new StringBuilder(spotifyTitle);
            sb.setLength(i);
            String s = sb.toString().trim();
            spotifyTitle = s;
        }

        String resultSinger = singer.replaceAll("\\s", "");
        List<String> singerList = spotifyMusic.getArtistName();
        List<String> isContainedList = new ArrayList<>();

        String spotifySinger = "";
        for (String s : singerList) {
            spotifySinger = s.replaceAll("\\s", "");
            System.out.println("===== 가수 검사 ====");
            System.out.println("resultSinger = " + resultSinger);
            System.out.println("spotifySinger = " + spotifySinger);
            if (resultSinger.contains(spotifySinger)) {
                isContainedList.add("singer contained");
            }
        }

        System.out.println("===== 제목 검사 ====");
        System.out.println("resultTitle = '" + resultTitle + "'");
        System.out.println("spotifyTitle = '" + spotifyTitle + "'");
        if (resultTitle.contains(spotifyTitle)) {
            isContainedList.add("title contained");
        }

        System.out.println(isContainedList);

        if (isContainedList.size() == 2 || isContainedList.contains("singer")) {
            System.out.println("imageUrl = " + spotifyMusic.getImageUrl().get(2));
            return spotifyMusic.getImageUrl().get(2);
        }
        return "/images/heart.png";
    }
    */


    /**
     * 노래 조회(API) 공통 로직
     */
    private List<MusicSearchDto> getMusicList(SearchType searchType, String keyword, BrandName brandName, int findType)
            throws JsonProcessingException {

        String requestUrl = MUSIC_API_URL;

//        if (StringUtils.hasText(searchType)) {
//            if (searchType.equals(TITLE)) {
//                requestUrl += TITLE_URL + keyword;
//            } else if (searchType.equals(SINGER)) {
//                requestUrl += SINGER_URL + keyword;
//            }
//        }

        if (brandName.equals(BrandName.TJ)) {
            requestUrl += TJ_URL;
        } else {
            requestUrl += KUMYOUNG_URL;
        }

        RestTemplate template = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(
                template.getForObject(requestUrl, String.class), new TypeReference<List<MusicSearchDto>>() {});
    }




}
