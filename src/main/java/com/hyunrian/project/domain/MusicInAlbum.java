package com.hyunrian.project.domain;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
//@NoArgsConstructor(access = AccessLevel.PROTECTED) -> queryrdsl join할 때 오류 발생
@NoArgsConstructor
public class MusicInAlbum {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "selected_music_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "music_id")
    private Music music; //나의 리스트에 담을 노래

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    private Album album;



    public MusicInAlbum(Music music, Album album) {
        this.music = music;
        this.album = album;
    }
}
