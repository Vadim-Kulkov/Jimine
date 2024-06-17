package com.jimine.jiminebackend.model.request.task;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UpdateTaskRequest {

    private String name;
    private String description;
    private Long taskStatusId;
    private Long taskTypeId;
    private Long taskPriorityId;
}
