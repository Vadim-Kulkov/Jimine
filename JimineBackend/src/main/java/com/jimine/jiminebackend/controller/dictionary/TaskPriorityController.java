package com.jimine.jiminebackend.controller.dictionary;

import com.jimine.jiminebackend.model.dictionary.TaskPriority;
import com.jimine.jiminebackend.request.BasePageRequest;
import com.jimine.jiminebackend.service.dictionary.TaskPriorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TaskPriorityController {

    private final TaskPriorityService service;

    @GetMapping("/dict/task-priority/page")
    public Page<TaskPriority> getPage(BasePageRequest request) {
        return service.getPage(request);
    }
}
