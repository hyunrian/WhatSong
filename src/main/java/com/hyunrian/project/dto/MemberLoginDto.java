package com.hyunrian.project.dto;

import com.hyunrian.project.domain.enums.Gender;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberLoginDto {

    private String email;
    private String password;

}
