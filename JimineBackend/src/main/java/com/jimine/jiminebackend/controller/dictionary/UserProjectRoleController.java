package com.jimine.jiminebackend.controller.dictionary;

import com.jimine.jiminebackend.model.entity.dictionary.UserProjectRole;
import com.jimine.jiminebackend.model.request.BasePageRequest;
import com.jimine.jiminebackend.service.dictionary.UserProjectRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserProjectRoleController {

    private final UserProjectRoleService service;

    @GetMapping("/dict/user-project-role/page")
    public Page<UserProjectRole> getPage(BasePageRequest request) {
        return service.getPage(request);
    }
}
