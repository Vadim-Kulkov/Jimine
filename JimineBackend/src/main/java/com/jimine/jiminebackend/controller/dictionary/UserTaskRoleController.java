package com.jimine.jiminebackend.controller.dictionary;

import com.jimine.jiminebackend.model.entity.dictionary.UserTaskRole;
import com.jimine.jiminebackend.model.request.BasePageRequest;
import com.jimine.jiminebackend.service.dictionary.UserTaskRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserTaskRoleController {

    private final UserTaskRoleService service;

    @GetMapping("/dict/user-task-role/page")
    public Page<UserTaskRole> getPage(BasePageRequest request) {
        return service.getPage(request);
    }
}
