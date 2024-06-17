package com.jimine.jiminebackend.controller.dictionary;

import com.jimine.jiminebackend.model.entity.dictionary.TaskStatus;
import com.jimine.jiminebackend.model.request.BasePageRequest;
import com.jimine.jiminebackend.service.dictionary.TaskStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TaskStatusController {

    private final TaskStatusService service;

    @GetMapping("/dict/task-status/page")
    public Page<TaskStatus> getPage(BasePageRequest request) {
        return service.getPage(request);
    }
}
