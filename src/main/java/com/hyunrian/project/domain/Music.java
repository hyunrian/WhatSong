package com.hyunrian.project.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Music {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "music_id")
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String singer;

    private String composer;
    private String lyricist;
    private String release;
    private LocalDateTime savedDate;
    private int likeCount;

    public void updateLikeCount(boolean isLiked) {
        if (isLiked) likeCount++;
        else likeCount--;
    }

}
