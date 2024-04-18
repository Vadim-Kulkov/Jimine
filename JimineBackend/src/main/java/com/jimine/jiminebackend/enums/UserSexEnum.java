package com.jimine.jiminebackend.enums;

import lombok.Getter;

@Getter
public enum UserSexEnum {

    MALE("Male"),
    FEMALE("Female");

    final String name;

    UserSexEnum(String name) {
        this.name = name;
    }
}
