package com.hyunrian.project.dto.music;

import lombok.Data;

import java.util.List;

@Data
public class SpotifySearchTrack extends SpotifySearch {

    private String trackName;
    private String trackId;
    private String imageUrl;
    private List<ArtistDto> artistList;

}
