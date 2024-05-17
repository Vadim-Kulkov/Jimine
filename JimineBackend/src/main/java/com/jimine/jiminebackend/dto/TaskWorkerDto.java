package com.jimine.jiminebackend.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TaskWorkerDto {

    private Long workerId;
    private String username;
    private Long userTaskRoleId;
    private String userTaskRoleName;
}
