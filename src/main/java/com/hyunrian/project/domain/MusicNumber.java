package com.hyunrian.project.domain;

import com.hyunrian.project.domain.enums.BrandName;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class MusicNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "music_number_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "music_id")
    private Music music;

    @Enumerated(EnumType.STRING)
    private BrandName brandName;

    private int number;
}
