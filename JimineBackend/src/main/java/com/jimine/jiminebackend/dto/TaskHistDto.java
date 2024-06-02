package com.jimine.jiminebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class TaskHistDto {

    private Long taskId;
    private String name;
    private String description;
    private Long taskProjectId;
    private String taskProjectName;
    private Long taskStatusId;
    private String taskStatusName;
    private Long taskTypeId;
    private String taskTypeName;
    private Long taskPriorityId;
    private String taskPriorityName;
    private LocalDateTime deletedAt;
}
