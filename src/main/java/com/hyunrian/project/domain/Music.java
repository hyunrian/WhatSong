package com.hyunrian.project.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "music")
    private List<MusicInAlbum> musicInAlbumList = new ArrayList<>();

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

    @Override
    public String toString() {
        return "Music{" +
                "trackId='" + trackId + '\'' +
                ", trackName='" + trackName + '\'' +
                ", artistId='" + artistId + '\'' +
                ", artistName='" + artistName + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", likeCount=" + likeCount +
                '}';
    }
}
