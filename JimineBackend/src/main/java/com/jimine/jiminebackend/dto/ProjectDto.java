package com.jimine.jiminebackend.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectDto {

    String name;
    String description;
    String projectStatusName;
}
