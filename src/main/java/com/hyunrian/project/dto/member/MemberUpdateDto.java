package com.hyunrian.project.dto.member;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberUpdateDto {

    private String nickname;

    private String password;

    private String imagePath;
}
