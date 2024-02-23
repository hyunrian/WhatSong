package com.hyunrian.project.dto;

import lombok.Data;

import java.util.List;

@Data
public class SpotifyAlbum {

    private String id;
    private String name;
    private String imageUrl;
    private String releaseDate;
    private List<ArtistDto> artistList;
    private List<TrackDto> trackList;
    private List<AlbumDto> albumList;
}
