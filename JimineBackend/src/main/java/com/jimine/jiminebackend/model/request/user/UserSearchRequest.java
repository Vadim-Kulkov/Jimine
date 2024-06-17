package com.jimine.jiminebackend.model.request.user;

import com.jimine.jiminebackend.model.request.BasePageRequest;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserSearchRequest extends BasePageRequest {

    private Long userId;
    private Long projectId;
    private Long roleId; // TODO rename to projectRoleId
    private Long taskId;
    private String email;
    private String username;
}
