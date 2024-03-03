package com.hyunrian.project.dto.member;

import com.hyunrian.project.validation.ValidationSequence;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberLoginDto {

    private String email;
    private String password;

}
