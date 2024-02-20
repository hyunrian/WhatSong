package com.hyunrian.project.domain;

import jakarta.persistence.*;

@Entity
public class AlbumTag {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "album_tag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    private Album album;

    private String content1;
    private String content2;
    private String content3;
    private String content4;
    private String content5;
}
