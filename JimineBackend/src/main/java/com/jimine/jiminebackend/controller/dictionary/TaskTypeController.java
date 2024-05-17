package com.jimine.jiminebackend.controller.dictionary;

import com.jimine.jiminebackend.model.dictionary.TaskType;
import com.jimine.jiminebackend.request.BasePageRequest;
import com.jimine.jiminebackend.service.dictionary.TaskTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TaskTypeController {

    private final TaskTypeService service;

    @GetMapping("/dict/task-type/page")
    public Page<TaskType> getPage(BasePageRequest request) {
        return service.getPage(request);
    }
}
