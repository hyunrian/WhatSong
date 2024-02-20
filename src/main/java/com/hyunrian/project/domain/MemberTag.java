package com.hyunrian.project.domain;

import jakarta.persistence.*;

@Entity
public class MemberTag {

    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "member_tag_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String content;

}
