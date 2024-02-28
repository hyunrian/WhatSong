package com.hyunrian.project.dto;

import com.hyunrian.project.domain.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import static com.hyunrian.project.validation.ValidationSequence.*;


@Getter @Setter
public class MemberJoinDto {

    @NotBlank
    @Email(groups = SecondCheckGroup.class)
    private String email;

    @NotBlank
    @Size(min = 2, max = 12, groups = SecondCheckGroup.class)
    private String nickname;

    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,15}$",
            groups = SecondCheckGroup.class)
    private String password;

    private Gender gender;
    private int birthYear;

    private String token;

}
