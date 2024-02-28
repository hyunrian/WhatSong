package com.hyunrian.project.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
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

//    private LocalDateTime savedDate;
    private int likeCount;


    /**
     * 비즈니스 로직
     */

    //좋아요 수 변경
    public void updateLikeCount(boolean isLiked) {
        if (isLiked) likeCount++;
        else likeCount--;
    }
}
