package com.jimine.jiminebackend.controller;

import com.jimine.jiminebackend.dto.TaskDto;
import com.jimine.jiminebackend.dto.TaskHistDto;
import com.jimine.jiminebackend.request.task.AddUserRequestWrapper;
import com.jimine.jiminebackend.request.task.CreateTaskRequest;
import com.jimine.jiminebackend.request.task.UpdateTaskRequest;
import com.jimine.jiminebackend.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping
public class TaskController {

    private final TaskService service;

    @GetMapping("users/me/tasks")
    public List<TaskDto> getPage(@RequestParam Map<String, String> searchParams) {
        return service.findAllByPrincipal(searchParams);
    }

    @GetMapping("projects/{projectId}/tasks")
    public List<TaskDto> getTasksByProjectId(@PathVariable Long projectId,
                                             @RequestParam Map<String, String> searchParams) {
        return service.findAllByProjectId(projectId, searchParams);
    }

    @PostMapping("tasks")
    public ResponseEntity<String> createTask(@RequestBody CreateTaskRequest request) { // TODO set principal as the creator
        return service.createTask(request);
    }

    @PostMapping("tasks/{taskId}/users")
    public ResponseEntity<String> addUsersToTheTask(@RequestBody AddUserRequestWrapper addUserRequestWrapper,
                                                    @PathVariable Long taskId) {
        return service.addWorkersToTheTask(new HashSet<>(addUserRequestWrapper.getWorkerRequestList()), taskId);
    }

    @DeleteMapping("tasks/{taskId}/users/{roleId}/{userId}")
    public ResponseEntity<String> removeUserFromTheTask(
            @PathVariable Long taskId,
            @PathVariable Long roleId,
            @PathVariable Long userId
    ) {
        return service.removeWorkerAssociationTheTask(taskId, roleId, userId);
    }

    @DeleteMapping("tasks/{taskId}")
    public ResponseEntity<String> deleteById(@PathVariable Long taskId) {
        return service.deleteById(taskId);
    }

    @PostMapping("tasks/{taskId}")
    public ResponseEntity<String> updateById(@RequestBody UpdateTaskRequest request, @PathVariable Long taskId) {
        return service.updateTask(request, taskId);
    }

    @GetMapping("tasks/hist")
    public List<TaskHistDto> getCurrentUserTaskHistory() {
        return service.getTaskHistory();
    }

}
