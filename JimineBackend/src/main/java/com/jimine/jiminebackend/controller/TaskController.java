package com.jimine.jiminebackend.controller;

import com.jimine.jiminebackend.dto.TaskDto;
import com.jimine.jiminebackend.request.task.AddUserRequestWrapper;
import com.jimine.jiminebackend.request.task.CreateTaskRequest;
import com.jimine.jiminebackend.request.task.TaskPageRequest;
import com.jimine.jiminebackend.request.task.UpdateTaskRequest;
import com.jimine.jiminebackend.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService service;

    @GetMapping("/page")
    public List<TaskDto> getPage(@RequestBody TaskPageRequest request) {
        return service.getTaskPage(request);
    }

    @PostMapping
    public ResponseEntity<String> createTask(@RequestBody CreateTaskRequest request) { // TODO set principal as the creator
        return service.createTask(request);
    }

    @PostMapping("/{taskId}/users")
    public ResponseEntity<String> addUsersToTheTask(@RequestBody AddUserRequestWrapper addUserRequestWrapper,
                                                    @PathVariable Long taskId) {
        return service.addWorkersToTheTask(new HashSet<>(addUserRequestWrapper.getWorkerRequestList()), taskId);
    }

    @DeleteMapping("/{taskId}/users/{roleId}/{userId}")
    public ResponseEntity<String> removeUserFromTheTask(
            @PathVariable Long taskId,
            @PathVariable Long roleId,
            @PathVariable Long userId
    ) {
        return service.removeWorkerAssociationTheTask(taskId, roleId, userId);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<String> deleteById(@PathVariable Long taskId) {
        return service.deleteById(taskId);
    }

    @PostMapping("/{taskId}")
    public ResponseEntity<String> updateById(@RequestBody UpdateTaskRequest request, @PathVariable Long taskId) {
        return service.updateTask(request, taskId);
    }
}
