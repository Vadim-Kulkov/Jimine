package com.jimine.jiminebackend.enums;

public enum UserProjectRoleEnum {

    ADMIN(0L, "Admin"),
    DEVELOPER(1L, "DEVELOPER"); // todo dev-levels, analyst

    private final Long userProjectRoleId;
    private final String userProjectRoleName;

    UserProjectRoleEnum(Long userProjectRoleId, String userProjectRoleName) {
        this.userProjectRoleId = userProjectRoleId;
        this.userProjectRoleName = userProjectRoleName;
    }

    public Long getUserProjectRoleId() {
        return userProjectRoleId;
    }

    public String getUserProjectRoleName() {
        return userProjectRoleName;
    }
}
