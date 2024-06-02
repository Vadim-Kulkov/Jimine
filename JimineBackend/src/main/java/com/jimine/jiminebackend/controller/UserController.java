package com.jimine.jiminebackend.controller;

import com.jimine.jiminebackend.dto.UserDto;
import com.jimine.jiminebackend.dto.UserTaskDto;
import com.jimine.jiminebackend.request.user.UserSearchRequest;
import com.jimine.jiminebackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService service;

    @GetMapping("/users/page") // todo rework
    public List<UserDto> getPage(@RequestBody UserSearchRequest request) {
        return service.getUserPage(request); // TODO required responseType is Page<UserDto>
    }

    @GetMapping("/tasks/{taskId}/users")
    public List<UserTaskDto> getUsersByTaskId(@PathVariable Long taskId) {
        return service.getUsersByTaskId(taskId);
    }

    @GetMapping("/projects/{projectId}/users")
    public List<UserTaskDto> getUsersByProjectId(@PathVariable Long projectId) {
        return service.getUsersByProjectId(projectId);
    }

    @PostMapping("/projects/{projectId}/add-user/{roleId}/{userId}")
    public ResponseEntity<String> addUserToTheProject(@PathVariable Long userId,
                                                      @PathVariable Long roleId,
                                                      @PathVariable Long projectId) {
        return service.addUserToTheProject(UserSearchRequest.builder()
                .projectId(projectId)
                .roleId(roleId)
                .userId(userId)
                .build()
        );
    }
}
