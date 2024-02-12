package com.hyunrian.project.domain;

import com.hyunrian.project.domain.enums.Gender;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true, length = 80)
    private String email;

    @Column(unique = true, length = 30)
    private String nickname;

    @Column(length = 30)
    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(length = 3)
    private int age;

    private LocalDateTime joinedDate;

    @OneToMany(mappedBy = "member")
    private List<LikeAlbum> likeAlbum = new ArrayList<>();


}
