package com.hyunrian.project.domain;

import jakarta.persistence.*;

@Entity
public class LikeAlbum {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "like_album_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    private Album album;
}
