package com.hyunrian.project.controller;

import com.hyunrian.project.domain.enums.SpotifyRequestType;
import com.hyunrian.project.dto.*;
import com.hyunrian.project.service.MusicService;
import com.hyunrian.project.utils.SpotifyUtils;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/music")
@RequiredArgsConstructor
@Slf4j
public class MusicControllor {

    private final MusicService musicService;

    @GetMapping("/search")
    public String search() {
        return "main/search/search";
    }

    //노래 검색 결과 조회
//    @GetMapping("/search/result")
//    public String searchResult(MusicSearchCondition searchDto, Model model) {
//        try {
//            List<MusicSearchDto> list = musicService.findBySearching(searchDto);
//            model.addAttribute("musicList", list);
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//        return "main/search/searchResult";
//    }

    //최신곡 조회
//    @GetMapping("/new")
//    public String viewLatest(BrandName brandName, Model model) throws IOException, ParseException, SpotifyWebApiException {
//        List<MusicSearchDto> list = musicService.findLatest(brandName);
//        model.addAttribute("musicList", list);
//        return "main/latest";
//    }

    //spotify
    @GetMapping("/spotify")
    public String viewSpotifyResult(SpotifyRequestType spotifyRequestType,
                                    MusicSearchCondition condition,
                                    Model model, HttpSession session) throws IOException, ParseException, SpotifyWebApiException, InterruptedException {

        String accessToken = getAccessTokenInSession(session);
        List<SpotifyMusic> list = musicService.findSpotifyBySearching(spotifyRequestType, condition, accessToken);
        model.addAttribute("musicList", list);
        return "main/search/test";
    }

    @GetMapping("/artist/{artistId}")
    public String viewArtistInfo(@PathVariable String artistId, Model model, HttpSession session) throws IOException, ParseException, SpotifyWebApiException {
        String accessToken = getAccessTokenInSession(session);
        SpotifyArtist artist = musicService.getArtistInfo(artistId, accessToken);
        model.addAttribute("artist", artist);
        return "main/search/testArtist";
    }

    @GetMapping("/album/{albumId}")
    public String viewAlbumInfo(@PathVariable String albumId, Model model, HttpSession session) throws IOException, ParseException, SpotifyWebApiException {
        String accessToken = getAccessTokenInSession(session);
        SpotifyAlbum album = musicService.getAlbumInfo(albumId, accessToken);
        model.addAttribute("album", album);
        return "main/search/testAlbum";
    }


    private String getAccessTokenInSession(HttpSession session) throws IOException, ParseException, SpotifyWebApiException {
        String accessToken = (String) session.getAttribute("accessToken");
        Date date = new Date(session.getCreationTime());
        log.info("accessToken={}", accessToken);
//        log.info("session.getCreationTime={}", creationTime);
        log.info("session.getCreationTime={}", date);
        if (accessToken == null) {
            accessToken = SpotifyUtils.getAccessToken();
            session.setAttribute("accessToken", accessToken);
        }
        return accessToken;
    }
}
