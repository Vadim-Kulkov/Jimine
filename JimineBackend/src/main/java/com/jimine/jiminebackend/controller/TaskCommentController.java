package com.jimine.jiminebackend.controller;

import com.jimine.jiminebackend.model.dto.TaskCommentDto;
import com.jimine.jiminebackend.model.request.CreateTaskCommentRequest;
import com.jimine.jiminebackend.model.request.TaskCommentRequest;
import com.jimine.jiminebackend.service.TaskCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class TaskCommentController {

    private final TaskCommentService service;

    @GetMapping("tasks/{taskId}/comments/page")
    public List<TaskCommentDto> getPage(TaskCommentRequest request,
                                        @PathVariable Long taskId) { // todo normal paging
        return service.getPage(request, taskId);
    }

    @PostMapping("tasks/{taskId}/comments")
    public ResponseEntity<String> createTaskComment(@RequestBody CreateTaskCommentRequest request,
                                                    @PathVariable Long taskId) {
        return service.createTaskComment(request, taskId);
    }

    @PostMapping("task-comments/{commentId}")
    public ResponseEntity<String> updateTaskComment(@RequestBody CreateTaskCommentRequest request,
                                                    @PathVariable Long commentId) {
        return service.updateTaskComment(request, commentId);
    }
}
