package com.hyunrian.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RelatedTrackDto {
    private String trackId;
    private String trackName;
    private String imageUrl;

    private String artistId;
    private String artistName;
}
