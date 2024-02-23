package com.hyunrian.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class AlbumDto {

    private String id;
    private String name;
    private String imageUrl;
    private String releaseDate;
}
