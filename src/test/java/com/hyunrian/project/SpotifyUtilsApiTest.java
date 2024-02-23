package com.hyunrian.project;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyunrian.project.dto.SpotifySearchTrack;
import com.hyunrian.project.utils.SpotifyUtils;
import com.neovisionaries.i18n.CountryCode;
import lombok.Data;
import lombok.ToString;
import org.apache.hc.core5.http.ParseException;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
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

public class SpotifyUtilsApiTest {

    @Test
    void apiTest() throws IOException, ParseException, SpotifyWebApiException {
        String clientId = "72eb6281b6c44ee2827ce0a129d9618c";
        String clientSecret = "ce81fa23cf66403b80a7d600a20822e1";
        URI uri = SpotifyHttpManager.makeUri("https://accounts.spotify.com/api/token");

        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectUri(uri)
                .build();

        ClientCredentialsRequest request = spotifyApi.clientCredentials().build();
        ClientCredentials credentials = request.execute();

        spotifyApi.setAccessToken(credentials.getAccessToken());
        String accessToken = spotifyApi.getAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.ACCEPT_LANGUAGE, "ko-KR");
//        headers.add(HttpHeaders.CONTENT_LANGUAGE, "ko-KR");
//        headers.setAcceptLanguageAsLocales(Collections.singletonList(Locale.KOREAN));

        Album album = spotifyApi.getAlbum("7mP7AFehQDonPKEQiXvpvB")
                .setPathParameter("market", "KR")
                .setHeader("Accept-Language", "ko-KR")
                .build()
                .execute();

        System.out.println(album.getName());
        ArtistSimplified[] artists = album.getArtists();
        for (ArtistSimplified artist : artists) {
            System.out.println(artist.getName());
        }
        Paging<TrackSimplified> tracks = album.getTracks();
        TrackSimplified[] items = tracks.getItems();
        for (TrackSimplified track : items) {
            System.out.println(track.getName());
        }
    }

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
    void apiTest_getTopTracks() throws IOException, ParseException, SpotifyWebApiException {
        String clientId = "72eb6281b6c44ee2827ce0a129d9618c";
        String clientSecret = "ce81fa23cf66403b80a7d600a20822e1";
        URI uri = SpotifyHttpManager.makeUri("https://accounts.spotify.com/api/token");

        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectUri(uri)
                .build();

        ClientCredentialsRequest request = spotifyApi.clientCredentials().build();
        ClientCredentials credentials = request.execute();

        spotifyApi.setAccessToken(credentials.getAccessToken());

        GetArtistsTopTracksRequest topTracksRequest =
                spotifyApi.getArtistsTopTracks("3HqSLMAZ3g3d5poNaI7GOU", CountryCode.KR)
                        .setHeader("Accept-Language", "ko-KR")
                        .build();
        se.michaelthelin.spotify.model_objects.specification.Track[] tracks = topTracksRequest.execute();
        for (se.michaelthelin.spotify.model_objects.specification.Track track : tracks) {
            System.out.println(track.getName());
        }

    }

    @Test
    void getToken() throws IOException, ParseException, SpotifyWebApiException {

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

//        GetArtistsTopTracksRequest topTracksRequest = spotifyApi.getArtistsTopTracks(clientId, countryCode).build();
//
//        Track[] tracks = topTracksRequest.execute();
//        System.out.println(tracks.length);

        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);;
        headers.add("Host", "api.spotify.com");
        headers.add("Content-type", "application/json");
        String body = "";
        String keyword = "지금 널 찾아가고 있어";

        String requestURL = "https://api.spotify.com/v1/search?type=track&q="
                + keyword
                + "&type=track"
                + "&market=KR"
                + "&limit=5";

        HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
        ResponseEntity<String> responseEntity = rest.exchange(requestURL, HttpMethod.GET, requestEntity, String.class);
        HttpStatus httpStatus = (HttpStatus) responseEntity.getStatusCode();
        int status = httpStatus.value(); //상태 코드가 들어갈 status 변수
        String response = responseEntity.getBody();
//        System.out.println("Response status: " + status);
//        System.out.println(response);

        //JSON -> 객체 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response);

        List<Track> trackList = new ArrayList<>();

        JsonNode itemsNode = rootNode.get("tracks").get("items");

        for (JsonNode itemNode : itemsNode) {
            JsonNode albumNode = itemNode.get("album");
            JsonNode artistsNode = albumNode.get("artists");

            List<String> artistsList = new ArrayList<>();
            for (JsonNode artistNode : artistsNode) {
                artistsList.add(artistNode.get("name").asText());
            }

            JsonNode imagesNode = albumNode.get("images");
            String imageUrl = imagesNode.get(0).get("url").asText();

            String releaseDate = albumNode.get("release_date").asText();
            String trackName = itemNode.get("name").asText();

            Track trackInfo = new Track(imageUrl, releaseDate, artistsList, trackName);
            trackList.add(trackInfo);
        }

        // trackInfoList를 사용하여 원하는 작업을 수행합니다.
        for (Track trackInfo : trackList) {
            System.out.println(trackInfo);
        }

        //가져온 트랙이 1개일 때
/*
        for (JsonNode item : items) {
            JsonNode album = item.get("album");
            JsonNode images = album.get("images");
            String imageUrl = images.get(0).get("url").asText(); // 첫 번째 이미지 URL 가져오기
            String releaseDate = album.get("release_date").asText();

            List<String> artists = new ArrayList<>();
            JsonNode artistsNode = album.get("artists");
            for (JsonNode artist : artistsNode) {
                artists.add(artist.get("name").asText());
            }

            String trackName = item.get("name").asText();

            Track track = new Track(imageUrl, releaseDate, artists, trackName);
            tracks.add(track);
        }

        for (Track track : tracks) {
            System.out.println(track);
        }
*/

    }

    class Track {
        private String imageUrl;
        private String releaseDate;
        private List<String> artists;
        private String trackName;

        public Track(String imageUrl, String releaseDate, List<String> artists, String trackName) {
            this.imageUrl = imageUrl;
            this.releaseDate = releaseDate;
            this.artists = artists;
            this.trackName = trackName;
        }

        @Override
        public String toString() {
            return "Track{" +
                    "imageUrl='" + imageUrl + '\'' +
                    ", releaseDate='" + releaseDate + '\'' +
                    ", artists=" + artists +
                    ", trackName='" + trackName + '\'' +
                    '}';
        }
    }


    @Test
    void jsonParsing() throws JsonProcessingException {
        String json = "{\"id\":2,\"name\":\"group1\",\"student_list\":[{\"name\":\"Danny\",\"classroom\":\"1-2\",\"hobby\":[\"golf\",\"tennis\",\"squash\"]},{\"name\":\"Tony\",\"classroom\":\"2-5\",\"hobby\":[\"football\",\"baseball\",\"basketball\"]}]}";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(json);

        List<StudentInfo> list = new ArrayList<>();

        JsonNode studentList = rootNode.get("student_list"); //반드시 첫번째 라인부터 시작해야 함
        for (JsonNode student : studentList) {
            String name = student.get("name").asText();
            List<String> hobbies = new ArrayList<>();
            JsonNode hobbyNode = student.get("hobby");
            for (JsonNode hobby : hobbyNode) {
                hobbies.add(hobby.asText());
            }
            StudentInfo studentInfo = new StudentInfo(name, hobbies);
            list.add(studentInfo);
        }

        for (StudentInfo studentInfo : list) {
            System.out.println("name:" + studentInfo.getName());
            System.out.println("hobbies:" + studentInfo.getHobbyList());
        }

        System.out.println(list);
    }

    @Data
    class StudentInfo {
        private String name;
        private List<String> hobbyList;

        public StudentInfo(String name, List<String> hobbyList) {
            this.name = name;
            this.hobbyList = hobbyList;
        }
    }

    @Test
    void getData() throws JsonProcessingException {
        String clientId = "72eb6281b6c44ee2827ce0a129d9618c";
        String clientSecret = "ce81fa23cf66403b80a7d600a20822e1";
        String url = "https://accounts.spotify.com/api/token";

        Map<String, Object> map = new HashMap<>();


        RestTemplate template = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        List<DataTest.MyMusic> searchList =
                objectMapper.readValue(template.getForObject(url, String.class),
                        new TypeReference<List<DataTest.MyMusic>>() {});





    }
}


