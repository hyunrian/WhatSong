package com.hyunrian.project.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MusicPreference {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "music_preference_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String genre;

    private String trackId;

    private LocalDateTime createdDate = LocalDateTime.now();

    public MusicPreference(Member member, String genre, String trackId) {
        this.member = member;
        this.genre = genre;
        this.trackId = trackId;
    }

    @Override
    public String toString() {
        return "MusicPreference{" +
                "genre='" + genre + '\'' +
                ", trackId='" + trackId + '\'' +
                '}';
    }
}
