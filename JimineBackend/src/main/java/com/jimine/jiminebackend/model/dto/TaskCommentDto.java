package com.jimine.jiminebackend.model.dto;

import com.jimine.jiminebackend.model.request.BasePageRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TaskCommentDto extends BasePageRequest {

    private Long id;
    private String commentName;
    private String content;
    private LocalDateTime createdAt;
    private Long userId;
    private String username;
}
