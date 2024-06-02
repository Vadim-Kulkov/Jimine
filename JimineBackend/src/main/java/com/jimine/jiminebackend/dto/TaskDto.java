package com.jimine.jiminebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
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
    private List<UserTaskDto> taskWorkers;

    public TaskDto(
            Long taskId, String name, String description, Long taskStatusId, String taskStatusName,
            Long taskTypeId, String taskTypeName, Long taskPriorityId, String taskPriorityName) {
        this(taskId, name, description, taskStatusId, taskStatusName, taskTypeId, taskTypeName, taskPriorityId, taskPriorityName, null, null);
    }

    public TaskDto(TaskDto toCopy) {
        this.taskId = toCopy.getTaskId();
        this.name = toCopy.getName();
        this.description = toCopy.getDescription();
        this.taskStatusId = toCopy.getTaskStatusId();
        this.taskStatusName = toCopy.getTaskStatusName();
        this.taskTypeId = toCopy.getTaskTypeId();
        this.taskTypeName = toCopy.getTaskTypeName();
        this.taskPriorityId = toCopy.getTaskPriorityId();
        this.taskPriorityName = toCopy.getTaskPriorityName();
        this.taskComments = toCopy.getTaskComments();
        this.taskWorkers = toCopy.getTaskWorkers();
    }
}

