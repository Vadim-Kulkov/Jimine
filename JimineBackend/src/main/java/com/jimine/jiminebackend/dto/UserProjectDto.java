package com.jimine.jiminebackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserProjectDto {

    private Long userId;
    private String username;
    private Long userProjectId;
    private String userProjectName;
}
