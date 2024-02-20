package com.hyunrian.project.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class MusicSearchDto {
    private String title;
    private String singer;
    private String composer;
    private String lyricist;
    private String release;
    private String no;
    private String brand;
    private String imageUrl;
}
