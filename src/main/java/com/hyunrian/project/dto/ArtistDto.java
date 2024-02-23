package com.hyunrian.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArtistDto {
    private String name;
    private String id;
    private String imageUrl;
    private String href;
}
