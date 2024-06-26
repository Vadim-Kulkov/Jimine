package com.jimine.jiminebackend.model.request.project;

import lombok.Data;

@Data
public class ProjectRequest {

    private String projectName;
    private String projectDescription;
    private Long projectStatusId;
    private Long creatorId;
}

