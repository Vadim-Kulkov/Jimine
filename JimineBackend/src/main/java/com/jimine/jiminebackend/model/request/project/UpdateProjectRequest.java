package com.jimine.jiminebackend.model.request.project;

import lombok.Data;

@Data
public class UpdateProjectRequest {

    private String name;
    private String description;
    private Long projectStatusId;
    private Long participantId;
}
