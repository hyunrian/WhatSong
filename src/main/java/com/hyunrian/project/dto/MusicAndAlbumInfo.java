package com.hyunrian.project.dto;

import lombok.*;

@Getter @Setter @ToString
@NoArgsConstructor
public class MusicAndAlbumInfo {

    private String albumName;

    private String title;
    private String singer;
    private String composer;
    private String lyricist;
    private String release;
    private String number;
    private int likeCount;

}
