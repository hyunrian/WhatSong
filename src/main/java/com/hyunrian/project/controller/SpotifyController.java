package com.hyunrian.project.controller;

import com.hyunrian.project.domain.enums.SearchType;
import com.hyunrian.project.dto.*;
import com.hyunrian.project.service.MusicService;
import com.hyunrian.project.service.SpotifyService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/music")
@RequiredArgsConstructor
@Slf4j
public class SpotifyController {

    private final SpotifyService spotifyService;

    @GetMapping("/start")
    public String viewFirstPage() {
        return "main/search/search";
    }

    //spotify - search (예외 처리)
    @GetMapping("/search")
    public String viewSearchResult(MusicSearchCondition condition, @RequestParam(defaultValue = "1") int page, Model model)
            throws IOException, ParseException, SpotifyWebApiException, InterruptedException {

        List list = spotifyService.getSearchResult(condition, page);

        if (list != null) {
            model.addAttribute("list", list);
            if (condition.getSearchType().equals(SearchType.TRACK)) {
                return "main/search/test";
            } else if (condition.getSearchType().equals(SearchType.ARTIST)) {
                return "main/search/testArtist";
            }
        } else {
            //null인 경우 예외처리
        }
        return "main/search/test";
    }

    @GetMapping("/track/{trackId}")
    public String viewTrackInfo(@PathVariable String trackId, Model model) throws IOException, ParseException, InterruptedException, SpotifyWebApiException {
        SpotifyMusic music = spotifyService.getMusicDetail(trackId);
        model.addAttribute("music", music);
        return "main/search/testTrack";
    }


    @GetMapping("/artist/{artistId}")
    public String viewArtistInfo(@PathVariable String artistId, @RequestParam(defaultValue = "1") int albumPage, Model model) throws IOException, ParseException, SpotifyWebApiException, InterruptedException {
        SpotifyArtist artist = spotifyService.getArtistInfo(artistId, albumPage);
        model.addAttribute("artist", artist);
        return "main/search/testArtistDetail";
    }

    @GetMapping("/album/{albumId}")
    public String viewAlbumInfo(@PathVariable String albumId, @RequestParam(defaultValue = "1") int albumPage, Model model) throws IOException, ParseException, SpotifyWebApiException, InterruptedException {
        SpotifyAlbum album = spotifyService.getAlbumInfo(albumId, albumPage);
        model.addAttribute("album", album);
        return "main/search/testAlbum";
    }

    @GetMapping("/new")
    public String viewNewRelease(@RequestParam(defaultValue = "1") int page, Model model) throws IOException, ParseException, InterruptedException, SpotifyWebApiException {
        List<SpotifyNewRelease> list = spotifyService.getNewReleaseAlbum(page);
        model.addAttribute("list", list);
        return "main/search/testNew";
    }
}
