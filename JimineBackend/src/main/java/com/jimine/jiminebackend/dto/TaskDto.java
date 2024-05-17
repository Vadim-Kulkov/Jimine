package com.jimine.jiminebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class TaskDto {

    private Long taskId;
    private String name;
    private String description;
    private Long taskStatusId;
    private String taskStatusName;
    private Long taskTypeId;
    private String taskTypeName;
    private Long taskPriorityId;
    private String taskPriorityName;
    private List<TaskCommentDto> taskComments;
    private List<TaskWorkerDto> taskWorkers;

    public TaskDto(
            Long taskId, String name, String description, Long taskStatusId, String taskStatusName,
            Long taskTypeId, String taskTypeName, Long taskPriorityId, String taskPriorityName) {
        this(taskId, name, description, taskStatusId, taskStatusName, taskTypeId, taskTypeName, taskPriorityId, taskPriorityName, null, null);
    }
}
