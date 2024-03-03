package com.hyunrian.project.utils;

import com.hyunrian.project.dto.music.*;
import com.neovisionaries.i18n.CountryCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.*;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.*;

import java.io.IOException;
import java.net.URI;
import java.util.*;

@Slf4j
public class SpotifyUtils {

    private static final String CLIENT_ID = "72eb6281b6c44ee2827ce0a129d9618c";
    private static final String CLIENT_SECRET = "ce81fa23cf66403b80a7d600a20822e1";
    private static final URI URI = SpotifyHttpManager.makeUri("https://accounts.spotify.com/api/token");
    private static final int LIMIT = 50;
    private static final String HEADER_NAME = "Accept-Language";
    private static final String HEADER_VALUE = "ko-KR";

    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(CLIENT_ID)
            .setClientSecret(CLIENT_SECRET)
            .setRedirectUri(URI)
            .build();

    //페이징 처리 필요, 배열 인덱스 length != 0 처리 필요

    /**
     * 토큰 얻기(모든 요청 전 토큰이 반드시 필요함)
     * ExpiresIn(3600 secs)
     */
    public static String getAccessToken() throws IOException, ParseException, SpotifyWebApiException {

        String accessToken = spotifyApi.getAccessToken();
        ClientCredentials credentials = spotifyApi.clientCredentials().build().execute();

        if (accessToken == null) {
            accessToken = credentials.getAccessToken();
        }

        spotifyApi.setAccessToken(accessToken);
        return accessToken;
    }

    public static List getMusicList(MusicSearchCondition condition, int page)
            throws IOException, ParseException, SpotifyWebApiException, InterruptedException {

        getAccessToken();
        Thread.sleep(200); //429 error 방지

        switch (condition.getSearchType()) {
            case TRACK:
                Paging<Track> trackPaging = spotifyApi.searchTracks(condition.getKeyword())
                        .limit(LIMIT)
                        .offset(LIMIT * (page - 1))
                        .setHeader(HEADER_NAME, HEADER_VALUE)
                        .build()
                        .execute();

                Track[] tracks = trackPaging.getItems();

                List<SpotifySearchTrack> trackList = new ArrayList<>();

                for (Track track : tracks) {
                    SpotifySearchTrack music = new SpotifySearchTrack();

                    music.setTrackName(track.getName());
                    music.setTrackId(track.getId());

                    AlbumSimplified album = track.getAlbum();
                    Image[] images = album.getImages();
                    music.setImageUrl(images[0].getUrl());

                    ArtistSimplified[] artists = track.getArtists();
                    List<ArtistDto> artistList = new ArrayList<>();
                    for (ArtistSimplified artist : artists) {
                        ArtistDto artistDto = new ArtistDto();

                        artistDto.setId(artist.getId());
                        artistDto.setName(artist.getName());

                        artistList.add(artistDto);
                    }
                    music.setArtistList(artistList);
                    trackList.add(music);
                }
                return trackList;

            case ARTIST:
                Paging<Artist> artistPaging = spotifyApi.searchArtists(condition.getKeyword())
                        .setHeader(HEADER_NAME, HEADER_VALUE)
                        .limit(LIMIT)
                        .offset(LIMIT * (page - 1))
                        .build()
                        .execute();

                Artist[] artists =  artistPaging.getItems();

                List<SpotifySearchArtist> artistList = new ArrayList<>();
                for (Artist artist : artists) {
                    SpotifySearchArtist artistDto = new SpotifySearchArtist();

                    artistDto.setId(artist.getId());
                    artistDto.setName(artist.getName());
                    Image[] images = artist.getImages();
                    if (images.length != 0) {
                        artistDto.setImageUrl(images[0].getUrl());
                    } else {
                        //default image
                    }
                    artistList.add(artistDto);
                }
                return artistList;
        }
        return null;
    }

    public static SpotifyArtist getArtistDetail(String artistId, int page) throws IOException, ParseException, SpotifyWebApiException, InterruptedException {
        getAccessToken();
        Thread.sleep(200);

        Artist artist = spotifyApi.getArtist(artistId)
                .setHeader(HEADER_NAME, HEADER_VALUE)
                .build()
                .execute();

        SpotifyArtist spotifyArtist = new SpotifyArtist();

        //기본 정보
        spotifyArtist.setId(artistId);
        spotifyArtist.setName(artist.getName());

        Image[] images = artist.getImages();
        spotifyArtist.setImageUrl(images[0].getUrl());

        String[] genres = artist.getGenres();
        List<String> genreList = new ArrayList<>();
        for (String genre : genres) {
            genreList.add(genre);
        }
        spotifyArtist.setGenreList(genreList);

        //top5 음악
        spotifyArtist.setTopTrackList(getTopTracks(artistId));

        //연관 아티스트
        spotifyArtist.setRelatedArtistList(getRelatedArtist(artistId));

        //아티스트의 앨범
        spotifyArtist.setArtistAlbumList(getArtistAlbum(artistId, page));

        return spotifyArtist;
    }

    public static SpotifyMusic getMusicDetail(String trackId) throws IOException, ParseException, SpotifyWebApiException, InterruptedException {
        getAccessToken();
        Thread.sleep(200);

        Track track = spotifyApi.getTrack(trackId)
                .setHeader(HEADER_NAME, HEADER_VALUE)
                .build()
                .execute();

        SpotifyMusic music = new SpotifyMusic();

        music.setTrackId(trackId);
        music.setTrackName(track.getName());

        AlbumSimplified album = track.getAlbum();
        AlbumDto albumDto = new AlbumDto();
        albumDto.setId(album.getId());
        Image[] images = album.getImages();
        albumDto.setImageUrl(images[0].getUrl());
        albumDto.setName(album.getName());
        music.setAlbum(albumDto);
        music.setReleaseDate(album.getReleaseDate());

        ArtistSimplified[] artists = track.getArtists();
        List<ArtistDto> artistList = new ArrayList<>();
        for (ArtistSimplified artist : artists) {
            ArtistDto artistDto = new ArtistDto();

            artistDto.setId(artist.getId());
            artistDto.setName(artist.getName());

            artistList.add(artistDto);
        }
        music.setArtistList(artistList);

        //현재 트랙과 비슷한 노래 추천
        music.setRelatedTrackList(getRelatedTrackList(trackId));

        //아티스트의 인기곡
        music.setTopTrackList(getTopTracks(artists[0].getId()));

        return music;
    }

    public static SpotifyAlbum getAlbumDetail(String albumId, int albumPage) throws IOException, ParseException, SpotifyWebApiException, InterruptedException {
        getAccessToken();
        Thread.sleep(200);

        Album album = spotifyApi.getAlbum(albumId)
                .setHeader(HEADER_NAME, HEADER_VALUE)
                .build()
                .execute();

        SpotifyAlbum spotifyAlbum = new SpotifyAlbum();

        spotifyAlbum.setId(albumId);
        spotifyAlbum.setName(album.getName());
        Image[] images = album.getImages();
        spotifyAlbum.setImageUrl(images[0].getUrl());
        spotifyAlbum.setReleaseDate(album.getReleaseDate());

        TrackSimplified[] tracks = album.getTracks().getItems();
        List<TrackDto> trackList = new ArrayList<>();
        for (TrackSimplified track : tracks) {
            TrackDto trackDto = new TrackDto();
            trackDto.setId(track.getId());
            trackDto.setName(track.getName());
            trackDto.setTrackNumber(track.getTrackNumber());
            trackList.add(trackDto);
        }
        spotifyAlbum.setTrackList(trackList);

        ArtistSimplified[] artists = album.getArtists();
        List<ArtistDto> artistList = new ArrayList<>();
        for (ArtistSimplified artist : artists) {
            ArtistDto artistDto = new ArtistDto();
            artistDto.setId(artist.getId());
            artistDto.setName(artist.getName());
            artistDto.setHref(artist.getHref());
            artistList.add(artistDto);
        }
        spotifyAlbum.setArtistList(artistList);

        //아티스트의 다른 앨범
        spotifyAlbum.setAlbumList(getArtistAlbum(artistList.get(0).getId(), albumPage));

        return spotifyAlbum;
    }

    public static List<SpotifyNewRelease> getNewReleaseAlbum(int page) throws IOException, ParseException, SpotifyWebApiException, InterruptedException {
        getAccessToken();
        Thread.sleep(200);

        Paging<AlbumSimplified> paging = spotifyApi.getListOfNewReleases()
                .setHeader(HEADER_NAME, HEADER_VALUE)
                .limit(LIMIT)
                .offset(LIMIT * (page - 1))
                .build()
                .execute();

        AlbumSimplified[] albums = paging.getItems();
        List<SpotifyNewRelease> list = new ArrayList<>();

        for (AlbumSimplified album : albums) {
            SpotifyNewRelease newAlbum = new SpotifyNewRelease();

            newAlbum.setAlbumId(album.getId());
            newAlbum.setAlbumName(album.getName());
            newAlbum.setHref(album.getHref());

            Image[] images = album.getImages();
            newAlbum.setImageUrl(images[0].getUrl());

            ArtistSimplified[] artists = album.getArtists();
            List<ArtistDto> artistList = new ArrayList<>();
            for (ArtistSimplified artist : artists) {
                ArtistDto artistDto = new ArtistDto();
                artistDto.setId(artist.getId());
                artistDto.setName(artist.getName());
                artistList.add(artistDto);
            }
            newAlbum.setArtistList(artistList);

            list.add(newAlbum);
        }

        return list;
    }

    public static List<SpotifySearchTrack> getGenreTracks() {
        return null;
    }

    public static List<SpotifySearchTrack> getRecommendation(String genres, String tracks) throws IOException, ParseException, SpotifyWebApiException, InterruptedException {
        getAccessToken();
        Thread.sleep(200);

        Recommendations reco = spotifyApi.getRecommendations()
                .setHeader(HEADER_NAME, HEADER_VALUE)
//                .seed_genres(genres)
//                .min_acousticness(0.9F)
//                .min_energy(0.9F)
                .seed_tracks(tracks)
//                .min_popularity(70)
                .limit(LIMIT)
                .build()
                .execute();

        List<SpotifySearchTrack> list = new ArrayList<>();

        Track[] trackArr = reco.getTracks();
        for (Track track : trackArr) {
            SpotifySearchTrack trackDto = new SpotifySearchTrack();
            trackDto.setTrackId(track.getId());
            trackDto.setTrackName(track.getName());

            Image[] images = track.getAlbum().getImages();
            if (images.length != 0) {
                trackDto.setImageUrl(images[0].getUrl());
            } else {
                //default image url
            }

            ArtistSimplified[] artists = track.getArtists();
            List<ArtistDto> artistList = new ArrayList<>();
            for (ArtistSimplified artist : artists) {
                ArtistDto artistDto = new ArtistDto();
                artistDto.setId(artist.getId());
                artistDto.setName(artist.getName());
                artistList.add(artistDto);
            }
            trackDto.setArtistList(artistList);

            list.add(trackDto);
        }
        return list;
    }

    public static List<String> getGenreSeeds() throws IOException, ParseException, SpotifyWebApiException {
        getAccessToken();

        String[] genres = spotifyApi.getAvailableGenreSeeds()
                .build()
                .execute();

        List<String> genreList = new ArrayList<>();
        for (String genre : genres) {
            genreList.add(genre);
        }
        return genreList;
    }

    private static List<TrackDto> getTopTracks(String artistId) throws IOException, ParseException, SpotifyWebApiException {
        getAccessToken();
        Track[] tracks = spotifyApi.getArtistsTopTracks(artistId, CountryCode.KR)
                .setHeader(HEADER_NAME, HEADER_VALUE)
                .build()
                .execute();

        List<TrackDto> list = new ArrayList<>();

        for (Track track : tracks) {
            TrackDto trackDto = new TrackDto();
            trackDto.setId(track.getId());
            trackDto.setName(track.getName());

            list.add(trackDto);
        }
        return list;
    }

    private static List<AlbumDto> getArtistAlbum(String artistId, int page) throws IOException, ParseException, SpotifyWebApiException {
        getAccessToken();

        Paging<AlbumSimplified> paging = spotifyApi.getArtistsAlbums(artistId)
                .setHeader(HEADER_NAME, HEADER_VALUE)
                .limit(LIMIT)
                .offset(LIMIT * (page - 1))
                .build()
                .execute();

        AlbumSimplified[] albums = paging.getItems();

        List<AlbumDto> list = new ArrayList<>();

        for (AlbumSimplified album : albums) {
            AlbumDto albumDto = new AlbumDto();

            albumDto.setId(album.getId());
            albumDto.setName(album.getName());
            albumDto.setReleaseDate(album.getReleaseDate());
            Image[] images = album.getImages();
            albumDto.setImageUrl(images[0].getUrl());

            list.add(albumDto);
        }

        return list;
    }

    private static List<ArtistDto> getRelatedArtist(String artistId) throws IOException, ParseException, SpotifyWebApiException {
        getAccessToken();

        Artist[] artists = spotifyApi.getArtistsRelatedArtists(artistId)
                .setHeader(HEADER_NAME, HEADER_VALUE)
                .build()
                .execute();

        List<ArtistDto> list = new ArrayList<>();

        for (Artist artist : artists) {
            ArtistDto artistDto = new ArtistDto();

            artistDto.setId(artist.getId());
            artistDto.setName(artist.getName());
            Image[] images = artist.getImages();
            artistDto.setImageUrl(images[0].getUrl());

            list.add(artistDto);
        }
        return list;
    }

    private static List<RelatedTrackDto> getRelatedTrackList(String trackId) throws IOException, SpotifyWebApiException, ParseException {
        Recommendations related = spotifyApi.getRecommendations()
                .setHeader(HEADER_NAME, HEADER_VALUE)
                .seed_tracks(trackId)
                .build()
                .execute();

        Track[] tracks = related.getTracks();
        List<RelatedTrackDto> relatedTrackList = new ArrayList<>();
        for (Track relatedTrack : tracks) {
            RelatedTrackDto trackDto = new RelatedTrackDto();

            trackDto.setTrackId(relatedTrack.getId());
            trackDto.setTrackName(relatedTrack.getName());
            Image[] albumImages = relatedTrack.getAlbum().getImages();
            trackDto.setImageUrl(albumImages[0].getUrl());
            trackDto.setArtistId(relatedTrack.getId());
            trackDto.setArtistName(relatedTrack.getName());

            relatedTrackList.add(trackDto);
        }
        return relatedTrackList;
    }

}
