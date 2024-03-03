package com.hyunrian.project.domain.enums.member;

import lombok.Getter;

@Getter
public enum MemberRole {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER"),
    UNAUTH("ROLE_UNAUTH_USER");

    private String value;

    MemberRole(String value) {
        this.value = value;
    }
}
