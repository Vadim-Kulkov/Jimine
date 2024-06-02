package com.jimine.jiminebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class TaskWorkerDto {

    private Long workerId;
    private String username;
    private Long userTaskRoleId;
    private String userTaskRoleName;
}
