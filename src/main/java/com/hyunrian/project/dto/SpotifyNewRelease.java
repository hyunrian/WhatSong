package com.hyunrian.project.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SpotifyNewRelease {

    private String albumId;
    private String albumName;
    private String imageUrl;
    private String href;
    private List<ArtistDto> artistList;
}
