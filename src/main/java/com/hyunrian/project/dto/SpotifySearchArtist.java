package com.hyunrian.project.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class SpotifySearchArtist extends SpotifySearch {

    private String id;
    private String name;
    private String imageUrl;

}
