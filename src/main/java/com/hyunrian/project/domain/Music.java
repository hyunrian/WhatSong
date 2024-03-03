package com.hyunrian.project.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Music {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "music_id")
    private Long id;

    @NotBlank
    private String trackId;

    @NotBlank
    private String trackName;

    @NotBlank
    private String artistId;

    @NotBlank
    private String artistName;

    private String imageUrl;

    private int likeCount;

    public Music(String trackId, String trackName, String artistId, String artistName, String imageUrl) {
        this.trackId = trackId;
        this.trackName = trackName;
        this.artistId = artistId;
        this.artistName = artistName;
        this.imageUrl = imageUrl;
    }


    /**
     * 비즈니스 로직
     */

    //좋아요 수 변경
    public void updateLikeCount(boolean isLiked) {
        if (isLiked) likeCount++;
        else likeCount--;
    }
}
