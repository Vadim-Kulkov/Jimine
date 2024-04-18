package com.jimine.jiminebackend.enums;

public enum ProjectStatusEnum {

    NEW(0L, "New"),
    DEVELOPING(1L, "Developing"),
    CLOSED(2L, "Closed");

    private final Long projectStatusId;
    private final String projectStatusName;

    ProjectStatusEnum(Long projectStatusId, String projectStatusName) {
        this.projectStatusId = projectStatusId;
        this.projectStatusName = projectStatusName;
    }

    public Long getProjectStatusId() {
        return projectStatusId;
    }

    public String getProjectStatusName() {
        return projectStatusName;
    }
}
