package com.jimine.jiminebackend.controller.dictionary;

import com.jimine.jiminebackend.model.entity.dictionary.ProjectStatus;
import com.jimine.jiminebackend.model.request.BasePageRequest;
import com.jimine.jiminebackend.service.dictionary.ProjectStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ProjectStatusController {

    private final ProjectStatusService service;

    @GetMapping("/dict/project-status/page")
    public Page<ProjectStatus> getPage(BasePageRequest request) {
        return service.getPage(request);
    }
}
