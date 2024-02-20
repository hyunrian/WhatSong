package com.hyunrian.project.domain;

import com.hyunrian.project.domain.enums.PublicStatus;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Album {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "album_id")
    private Long id;

    private String albumName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL)
    private List<SelectedMusic> selectedMusic = new ArrayList<>();

//    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL)
//    private LikeAlbum likeAlbum;

    private int likeCount;

    @Enumerated(EnumType.STRING)
    private PublicStatus publicStatus;

    //좋아요 로직
    public void updateLikeCount(boolean isLiked) {
        if (isLiked) likeCount++;
        else likeCount--;
    }

}
