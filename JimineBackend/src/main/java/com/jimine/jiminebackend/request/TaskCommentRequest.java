package com.jimine.jiminebackend.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TaskCommentRequest extends BasePageRequest {

    private Long commentId;
    private String commentName;
    private Long UserId;
}
