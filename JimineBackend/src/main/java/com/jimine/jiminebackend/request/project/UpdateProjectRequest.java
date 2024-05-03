package com.jimine.jiminebackend.request.project;

import lombok.Data;

@Data
public class UpdateProjectRequest {

    private String name;
    private String description;
    private Long projectStatusId;
    private Long participantId;
}
