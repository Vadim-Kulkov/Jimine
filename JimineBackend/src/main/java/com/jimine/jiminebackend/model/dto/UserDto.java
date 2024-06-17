package com.jimine.jiminebackend.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Data
public class UserDto {

    private Long id;
    private String username;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private Long userInfoId;
    private Set<Long> commentIds;
    private Set<Long> roleIds;
    private Set<Long> projectIds;
    private Set<Long> taskIds;
}
