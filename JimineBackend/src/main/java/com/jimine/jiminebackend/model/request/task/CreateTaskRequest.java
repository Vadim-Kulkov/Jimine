package com.jimine.jiminebackend.model.request.task;

import com.jimine.jiminebackend.model.request.TaskWorkerRequest;
import lombok.Data;

import java.util.List;

@Data
public class CreateTaskRequest {

    private Long projectId;
    private String name;
    private String description;
    private Long taskStatusId;
    private Long taskTypeId;
    private Long taskPriorityId;
    private List<TaskWorkerRequest> workerRequestSet;
}
