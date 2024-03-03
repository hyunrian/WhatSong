package com.hyunrian.project.domain;

import com.hyunrian.project.domain.enums.member.Gender;
import com.hyunrian.project.domain.enums.member.MemberStatus;
import com.hyunrian.project.dto.member.MemberJoinDto;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true, length = 80)
    private String email;

    @Column(unique = true, length = 30)
    private String nickname;

    @Column(length = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(length = 3)
    private int birthYear;

    private String imagePath;

    @Enumerated(EnumType.STRING)
    private MemberStatus status = MemberStatus.UNAUTH;

    private String token;

    @Column()
    private LocalDateTime joinedDate = LocalDateTime.now();

    @OneToMany(mappedBy = "member")
    private List<LikeAlbum> likeAlbum = new ArrayList<>();

    /**
     * 비즈니스 로직
     */

    //회원가입
    public Member getJoinMember(MemberJoinDto joinDto) {
        this.email = joinDto.getEmail();
        this.nickname = joinDto.getNickname();
        this.password = joinDto.getPassword();
        this.gender = joinDto.getGender();
        this.birthYear = joinDto.getBirthYear();
        this.token = joinDto.getToken();
        return this;
    }

    public void updateStatus() {
        this.status = MemberStatus.AUTH;
    }

    public void updateToken(String token) {
        this.token = token;
    }

    //개인정보 업데이트
    public void updateMember(String nickname, String password, String imagePath) {
        if (nickname != null) this.nickname = nickname;
        if (password != null) this.password = password;
        if (imagePath != null) this.imagePath = imagePath;
    }

    //임시 비밀번호로 변경
    public void changePwToTemp(String temp) {
        this.password = temp;
    }

}
