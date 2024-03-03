package com.hyunrian.project.utils;

import lombok.extern.slf4j.Slf4j;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;

import java.net.URI;

@Slf4j
public class SpotifyUtilsDirect {

    private static final String CLIENT_ID = "72eb6281b6c44ee2827ce0a129d9618c";
    private static final String CLIENT_SECRET = "ce81fa23cf66403b80a7d600a20822e1";
    private static final URI URI_TOKEN = SpotifyHttpManager.makeUri("https://accounts.spotify.com/api/token");

//    private static String accessToken = "";
    private static SpotifyApi spotifyApi;
/*

    */
/**
     * 토큰 얻기(모든 요청 전 토큰이 반드시 필요함)
     * 토큰이 만료되면 자동으로 얻도록 해야 함
     *//*

    public static String getAccessToken() throws IOException, ParseException, SpotifyWebApiException {
        spotifyApi = new SpotifyApi.Builder()
                .setClientId(CLIENT_ID)
                .setClientSecret(CLIENT_SECRET)
                .setRedirectUri(URI_TOKEN)
                .build();

        ClientCredentialsRequest request = spotifyApi.clientCredentials().build();
        ClientCredentials credentials = request.execute();

        String accessToken = credentials.getAccessToken();
        spotifyApi.setAccessToken(accessToken);

        return accessToken;
    }

    private static HttpHeaders getHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();

        headers.add("Authorization", "Bearer " + accessToken);;
        headers.add("Host", "api.spotify.com");
        headers.add("Content-type", "application/json");
        headers.setAcceptLanguageAsLocales(Collections.singletonList(Locale.KOREAN));

        return headers;
    }

    private static void apiTest() throws IOException, ParseException, SpotifyWebApiException {
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId(CLIENT_ID)
                .setClientSecret(CLIENT_SECRET)
                .setRedirectUri(URI_TOKEN)
                .build();

        ClientCredentialsRequest request = spotifyApi.clientCredentials().build();
        ClientCredentials credentials = request.execute();

        spotifyApi.setAccessToken(credentials.getAccessToken());
        String accessToken = spotifyApi.getAccessToken();

        Album album = spotifyApi.getAlbum("7mP7AFehQDonPKEQiXvpvB").build().execute();
        System.out.println(album.getName());

    }

    private static String getSearchRequestURL(SpotifyRequestType spotifyType, MusicSearchCondition condition) {

        String requestURL = "";

        switch (spotifyType) {
            case SEARCH:
                requestURL = SEARCH
                        + QUERY + condition.getKeyword()
                        + "&" + LIMIT + "50&";
                SearchType searchType = condition.getSearchType();
                if (searchType.equals(SearchType.ARTIST)) {
                    requestURL += TYPE_ARTIST;
                } else if (searchType.equals(SearchType.ALBUM)) {
                    requestURL += TYPE_ALBUM;
                } else {
                    requestURL += TYPE_TRACK;
                }
                break;
            case NEW_RELEASE:
                requestURL = NEW_RELEASE + LIMIT + "20";
                break;
            case RECOMMENDATION:
                break;
            case ARTIST:
                break;
        }
        return requestURL;
    }

    private static String getArtistRequestURL(String artistId) {
        return ARTIST + ONE_ARTIST + artistId;
    }

    private static String getAlbumRequestURL(String albumId) {
        return ALBUM + albumId;
    }


    public static List getMusicList(SpotifyRequestType spotifyType, MusicSearchCondition condition, String accessToken)
            throws IOException, ParseException, SpotifyWebApiException, InterruptedException {

        String requestURL = getSearchRequestURL(spotifyType, condition);
        String jsonData = getJsonData(requestURL, accessToken);

        return jsonToTrackList(jsonData);
    }

    private static String getJsonData(String requestURL, String accessToken)
            throws IOException, ParseException, SpotifyWebApiException, InterruptedException {

        HttpHeaders headers = getHeaders(accessToken);
        String body = "";
        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);

        RestTemplate template = new RestTemplate();
        Thread.sleep(200); //429 error 방지
        ResponseEntity<String> response = template.exchange(requestURL, HttpMethod.GET, requestEntity, String.class);

        HttpStatus httpStatus = (HttpStatus) response.getStatusCode();
        int status = httpStatus.value(); //상태 코드가 들어갈 status 변수
        String jsonData = response.getBody();

        log.info("status={}", status);

        switch (status) {
            case 401: //Unauthorized
                getAccessToken();
                return template.exchange(requestURL, HttpMethod.GET, requestEntity, String.class).getBody();
            case 429: //Too Many Requests
                int retryAfter = Integer.valueOf(response.getHeaders().getFirst("retry-after"));
                if (retryAfter <= 10) {
                    Thread.sleep(retryAfter * 1000);
                    ResponseEntity<String> retryResponse =
                            template.exchange(requestURL, HttpMethod.GET, requestEntity, String.class);
                    if (response.getStatusCode().value() == 200) {
                        return retryResponse.getBody();
                    } else {
                        log.error("Spotify API call - Retry Failed: retryAfter={}", retryAfter);
                        throw new Spotify429Error("Retry Failed", 429);
                    }
                } else {
                    log.error("Spotify API call - First Try Failed: retryAfter={}", retryAfter);
                    throw new Spotify429Error("Retry Failed", 429);
                }
        }

        return jsonData;
    }

    public static SpotifyArtist getArtistInfo(String artistId, String accessToken) throws JsonProcessingException {

        HttpHeaders headers = getHeaders(accessToken);

        String requestURL = getArtistRequestURL(artistId);
        String body = "";
        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);

        RestTemplate template = new RestTemplate();
        ResponseEntity<String> response = template.exchange(requestURL, HttpMethod.GET, requestEntity, String.class);

        HttpStatus httpStatus = (HttpStatus) response.getStatusCode();
        int status = httpStatus.value(); //상태 코드가 들어갈 status 변수
        String jsonData = response.getBody();
//        System.out.println("Response status: " + status);

        return jsonToArtist(jsonData);
    }

    public static SpotifyAlbum getAlbumInfo(String albumId, String accessToken) throws JsonProcessingException {
        HttpHeaders headers = getHeaders(accessToken);

        String requestURL = getAlbumRequestURL(albumId);
        String body = "";
        HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);

        RestTemplate template = new RestTemplate();
        ResponseEntity<String> response = template.exchange(requestURL, HttpMethod.GET, requestEntity, String.class);

        HttpStatus httpStatus = (HttpStatus) response.getStatusCode();
        int status = httpStatus.value(); //상태 코드가 들어갈 status 변수
        String jsonData = response.getBody();
//        System.out.println("Response status: " + status);

        return jsonToAlbum(jsonData);
    }

    private static List jsonToTrackList(String jsonData) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonData);

        List<SpotifyMusic> musicList = new ArrayList<>();

        JsonNode itemsNode = rootNode.get("tracks").get("items");

        for (JsonNode itemNode : itemsNode) {
            SpotifyMusic spotifyMusic = new SpotifyMusic();

            //trackName
            String trackName = itemNode.get("name").asText();
            spotifyMusic.setTrackName(trackName);

            //trackId
            String trackId = itemNode.get("id").asText();
            spotifyMusic.setTrackId(trackId);

            //albumId
            JsonNode albumNode = itemNode.get("album");
            String albumId = albumNode.get("id").asText();
            //albumName
            String albumName = albumNode.get("name").asText();
            spotifyMusic.setAlbum(new AlbumDto(albumId, albumName));

            //albumImage
            JsonNode imagesNodes = albumNode.get("images");
            ArrayList<String> imagesList = new ArrayList<>();
            for (JsonNode imageNode : imagesNodes) {
                imagesList.add(imageNode.get("url").asText());
            }
            spotifyMusic.setImageUrl(imagesList);

            //releaseDate
            String releaseDate = albumNode.get("release_date").asText();
            spotifyMusic.setReleaseDate(releaseDate);

            //artistId, artistName(items -> artists)
            JsonNode artistsNode = itemNode.get("artists");

            List<ArtistDto> artistList = new ArrayList<>();

            for (JsonNode artist : artistsNode) {
                artistList.add(new ArtistDto(artist.get("name").asText(), artist.get("id").asText()));
            }
            spotifyMusic.setArtistList(artistList);

            //artistId, artistName(items -> album -> artists)
//            JsonNode artistsNode = albumNode.get("artists");
//            List<String> artistNameList = new ArrayList<>();
//            List<String> artistIdList = new ArrayList<>();
//            for (JsonNode artist : artistsNode) {
//                artistNameList.add(artist.get("name").textValue());
//                artistIdList.add(artist.get("id").textValue());
//            }

            musicList.add(spotifyMusic);
        }

        return musicList;
    }

    private static SpotifyArtist jsonToArtist(String jsonData) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonData);

        SpotifyArtist artist = new SpotifyArtist();

        String name = rootNode.get("name").asText();
        artist.setName(name);

        JsonNode imagesNode = rootNode.get("images");
        for (JsonNode images : imagesNode) {
            String url = images.get("url").asText();
            artist.setImageUrl(url);
        }

        String popularity = rootNode.get("popularity").asText();
        artist.setPopularity(popularity);

        JsonNode genresNode = rootNode.get("genres");
        List<String> genreList = new ArrayList<>();
        for (JsonNode genres : genresNode) {
            String genre = genres.asText();
            genreList.add(genre);
        }
        artist.setGenreList(genreList);

        String id = rootNode.get("id").asText();
        artist.setId(id);

        return artist;
    }

    private static SpotifyAlbum jsonToAlbum(String jsonData) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonData);

        SpotifyAlbum album = new SpotifyAlbum();

        String id = rootNode.get("id").asText();
        album.setId(id);

        String imageUrl = rootNode.get("images").get(0).get("url").asText();
        album.setImageUrl(imageUrl);

        String name = rootNode.get("name").asText();
        album.setName(name);

        String releaseDate = rootNode.get("release_date").asText();
        album.setReleaseDate(releaseDate);

        JsonNode artistsNode = rootNode.get("artists");
        List<ArtistDto> artistList = new ArrayList<>();
        for (JsonNode artist : artistsNode) {
            String artistId = artist.get("id").asText();
            String artistName = artist.get("name").asText();
            artistList.add(new ArtistDto(artistId, artistName));
        }
        album.setArtistList(artistList);

        JsonNode itemsNode = rootNode.get("tracks").get("items");
        List<TrackDto> trackList = new ArrayList<>();
        for (JsonNode item : itemsNode) {
            String trackId = item.get("id").asText();
            String trackName = item.get("name").asText();
            String trackNumber = item.get("track_number").asText();
            trackList.add(new TrackDto(trackId, trackName, trackNumber));
        }
        album.setTrackList(trackList);

        return album;
    }

*/

}
