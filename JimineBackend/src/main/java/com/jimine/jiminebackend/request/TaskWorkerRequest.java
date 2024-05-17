package com.jimine.jiminebackend.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TaskWorkerRequest {

    private Long workerId;
    private Long userTaskRoleId;
}
