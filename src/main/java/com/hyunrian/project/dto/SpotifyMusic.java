package com.hyunrian.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SpotifyMusic {

    private String trackName;
    private String trackId;
    private String releaseDate;
    private String imageUrl;

    private AlbumDto album;
    private List<ArtistDto> artistList;

    private List<RelatedTrackDto> relatedTrackList;
    private List<TrackDto> topTrackList;

}
