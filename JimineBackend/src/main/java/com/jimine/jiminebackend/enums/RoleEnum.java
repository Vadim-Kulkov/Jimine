package com.jimine.jiminebackend.enums;

import lombok.Getter;

@Getter
public enum RoleEnum {

    ROLE_ADMIN(0L, "ADMIN"),
    ROLE_USER(1L, "USER");

    private final Long roleId;
    private final String roleName;

    RoleEnum(Long roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }
}
