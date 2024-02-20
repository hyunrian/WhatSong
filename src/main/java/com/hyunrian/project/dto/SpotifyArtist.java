package com.hyunrian.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SpotifyArtist {

    private String id;
    private String name;
    private String popularity;
    private String imageUrl;
    private List<String> genreList;
}
