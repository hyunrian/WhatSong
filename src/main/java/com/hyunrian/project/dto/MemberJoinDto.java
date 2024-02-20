package com.hyunrian.project.dto;

import com.hyunrian.project.domain.enums.Gender;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberJoinDto {

    private String email;
    private String nickname;
    private String password;
    private Gender gender;
    private int age;

}
