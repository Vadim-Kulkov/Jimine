package com.jimine.jiminebackend.request.task;

import com.jimine.jiminebackend.request.BasePageRequest;
import lombok.Data;

@Data
public class TaskPageRequest extends BasePageRequest {

    private Long taskId;
    private String taskName;
    private Long taskStatusId;
    private Long TaskTypeId;
    private Long taskPriorityId;
    private Long projectId;
    private Long workerId;
}
