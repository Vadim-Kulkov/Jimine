package com.jimine.jiminebackend.request.task;

import lombok.Data;

@Data
public class CreateTaskRequest {

    private Long projectId;
    private String name;
    private String description;
    private Long taskStatusId;
    private Long taskTypeId;
    private Long taskPriorityId;
}
