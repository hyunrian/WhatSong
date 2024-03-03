package com.hyunrian.project.dto.music;

import lombok.Data;

@Data
public class SpotifySearchArtist extends SpotifySearch {

    private String id;
    private String name;
    private String imageUrl;

}
