package com.hyunrian.project.domain.enums.music;

public class SpotifyURLConstants {

    public static final String SEARCH = "https://api.spotify.com/v1/search?";
    public static final String RECOMMENDATION = "https://api.spotify.com/v1/recommendations?";
    public static final String ARTIST = "https://api.spotify.com/v1/artists";
    public static final String NEW_RELEASE = "https://api.spotify.com/v1/browse/new-releases?";
    public static final String ALBUM = "https://api.spotify.com/v1/albums/";

    //search(1~50), recommendation(1~100), new_release(1~50)
    public static final String LIMIT = "limit=";

    /**
     * ARTIST(ONLY ID of the artist required)
     */
    public static final String ONE_ARTIST = "/"; // v1/artists/aaa
    public static final String SEVERAL_ARTISTS = "?ids="; // ids=aaa,bbb,ccc (max 50)
    public static final String RELATED_ARTIST = "/related-artists"; // v1/artists/aaa/related-artists

    /**
     * SEARCH(query and type required)
     */
    public static final String QUERY = "q=";
    public static final String TYPE_ALBUM = "type=album";
    public static final String TYPE_ARTIST = "type=artist";
    public static final String TYPE_TRACK = "type=track";

    /**
     * RECOMMENDATION
     */
    public static final String SEED_ARTIST = "seed_artists=";
    public static final String SEED_GENRE = "seed_genre=";
    public static final String SEED_TRACK = "seed_tracks=";

    public static final String MIN_POPULAR = "min_popularity="; //0~1
    public static final String MAX_POPULAR = "max_popularity="; //0~1

    public static final String MIN_TEMPO = "min_tempo="; //BPM
    public static final String MAX_TEMPO = "max_tempo="; //BPM

    public static final String MIN_ENERGY = "min_energy="; //0~1
    public static final String MAX_ENERGY = "max_energy="; //0~1

    public static final String MIN_ACOUSTICNESS = "min_acousticness="; //0~1
    public static final String MAX_ACOUSTICNESS = "max_acousticness="; //0~1

    public static final String MIN_DANCEABILITY = "min_danceability="; //0~1
    public static final String MAX_DANCEABILITY = "max_danceability="; //0~1

    public static final String MIN_INSTRUMENT = "min_instrumentalness="; //0~1
    public static final String MAX_INSTRUMENT = "max_instrumentalness="; //0~1
}
