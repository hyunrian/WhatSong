package com.hyunrian.project.domain;

import com.hyunrian.project.domain.enums.BrandName;
import com.hyunrian.project.domain.enums.Gender;
import com.hyunrian.project.dto.MemberJoinDto;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.hyunrian.project.domain.enums.BrandName.*;

@Entity
@Getter
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "member_id")
    private Long id;

//    @Column(unique = true, length = 80)
    private String email;

//    @Column(unique = true, length = 30)
    private String nickname;

    @Column(length = 30)
    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(length = 3)
    private int age;

    private String imagePath;

    @Enumerated(value = EnumType.STRING)
    private BrandName brandName;

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
        this.age = joinDto.getAge();
        return this;
    }

    //개인정보 업데이트
    public void updateMember(String nickname, String password, String imagePath) {
        if (nickname != null) this.nickname = nickname;
        if (password != null) this.password = password;
        if (imagePath != null) this.imagePath = imagePath;
    }

    //노래방 브랜드 변경
    public void updateMusicBrand(BrandName brand) {
        switch (brand) {
            case TJ:
                this.brandName = TJ;
                break;
            case KUMYOUNG:
                this.brandName = KUMYOUNG;
        }
    }

}
