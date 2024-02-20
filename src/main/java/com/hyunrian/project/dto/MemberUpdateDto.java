package com.hyunrian.project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberUpdateDto {

    private String nickname;

    private String password;

    private String imagePath;
}
