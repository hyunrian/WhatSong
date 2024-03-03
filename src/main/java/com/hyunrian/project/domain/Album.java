package com.hyunrian.project.domain;

import com.hyunrian.project.domain.enums.album.PublicStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Album {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "album_id")
    private Long id;

    private String albumName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "music_id")
    private List<Music> musicList = new ArrayList<>();

    private int likeCount;

    @Enumerated(EnumType.STRING)
    private PublicStatus publicStatus = PublicStatus.PUBLIC;

    public Album(String albumName, Member member) {
        this.member = member;
        this.albumName = albumName;
    }

    //좋아요 로직
    public void updateLikeCount(boolean isLiked) {
        if (isLiked) likeCount++;
        else likeCount--;
    }

    public void addMusicToAlbum(Music music) {
        musicList.add(music);
    }

}
