package com.hyunrian.project.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MusicInAlbum {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "music_in_album_id")
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

    public void setAlbum(Album album) {
        this.album = album;
    }

    @Override
    public String toString() {
        return "MusicInAlbum{" +
                "music=" + music +
                '}';
    }

}
