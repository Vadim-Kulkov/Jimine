package com.jimine.jiminebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserTaskDto {

    private Long userId;
    private String username;
    private Long userTaskRoleId;
    private String userTaskRoleName;
}
