package com.hyunrian.project;

import com.hyunrian.project.dto.SpotifySearchTrack;
import com.hyunrian.project.utils.SpotifyUtils;
import com.neovisionaries.i18n.CountryCode;
import org.apache.hc.core5.http.ParseException;
import org.junit.jupiter.api.Test;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.model_objects.specification.*;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import se.michaelthelin.spotify.requests.data.artists.GetArtistsTopTracksRequest;

import java.io.IOException;
import java.net.URI;
import java.util.*;

import static org.assertj.core.api.Assertions.*;

public class SpotifyUtilsApiTest {

    @Test
    void getGenres() throws IOException, ParseException, SpotifyWebApiException {
        List<String> genreSeeds = SpotifyUtils.getGenreSeeds();
        for (String genreSeed : genreSeeds) {
            System.out.println(genreSeed);
        }
    }

    @Test
    void getRecommendation() throws IOException, ParseException, InterruptedException, SpotifyWebApiException {
        List<SpotifySearchTrack> list = SpotifyUtils.getRecommendation(null, "1J0NAemu98Bg5y39sqqfMI,4OKJVnIyO8KRq182dstNOI");
        for (SpotifySearchTrack searchTrack : list) {
            System.out.println(searchTrack.getTrackName() + " - " + searchTrack.getArtistList().get(0).getName());
        }
    }

    @Test
    void pagination() throws IOException, ParseException, InterruptedException, SpotifyWebApiException {
        SpotifyApi spotifyApi = getApi();

        //최초 요청
        int htmlPage = 1;

        Paging<Track> trackPaging = spotifyApi.searchTracks("세븐틴")
                .limit(20)
                .offset(20 * (htmlPage - 1))
                .setHeader("Accept-Language", "ko-KR")
                .build()
                .execute();

        assertThat(trackPaging.getOffset()).isEqualTo(0);
    }

    @Test
    void apiTest_getTopTracks() throws IOException, ParseException, SpotifyWebApiException {
        SpotifyApi spotifyApi = getApi();

        GetArtistsTopTracksRequest topTracksRequest =
                spotifyApi.getArtistsTopTracks("3HqSLMAZ3g3d5poNaI7GOU", CountryCode.KR)
                        .setHeader("Accept-Language", "ko-KR")
                        .build();
        Track[] tracks = topTracksRequest.execute();
        for (Track track : tracks) {
            assertThat(track.getName()).isNotNull();
        }
    }

    @Test
    SpotifyApi getApi() throws IOException, ParseException, SpotifyWebApiException {

        String clientId = "72eb6281b6c44ee2827ce0a129d9618c";
        String clientSecret = "ce81fa23cf66403b80a7d600a20822e1";
        URI uri = SpotifyHttpManager.makeUri("https://accounts.spotify.com/api/token");
        CountryCode countryCode = CountryCode.KR;

        // For all requests an access token is needed
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectUri(uri)
                .build();

        ClientCredentialsRequest request = spotifyApi.clientCredentials().build();

        ClientCredentials credentials = request.execute();
        spotifyApi.setAccessToken(credentials.getAccessToken());
        String accessToken = spotifyApi.getAccessToken();

        spotifyApi.setAccessToken(accessToken);

        return spotifyApi;
    }

}


