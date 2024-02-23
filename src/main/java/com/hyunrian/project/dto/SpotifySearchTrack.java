package com.hyunrian.project.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class SpotifySearchTrack extends SpotifySearch {

    private String trackName;
    private String trackId;
    private String imageUrl;
    private List<ArtistDto> artistList;

}
