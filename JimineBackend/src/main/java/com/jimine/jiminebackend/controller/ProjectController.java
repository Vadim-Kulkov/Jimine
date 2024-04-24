package com.jimine.jiminebackend.controller;

import com.jimine.jiminebackend.dto.ProjectDto;
import com.jimine.jiminebackend.request.ProjectRequest;
import com.jimine.jiminebackend.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RequiredArgsConstructor
@RestController()
public class ProjectController {

    private final ProjectService service;

    @PostMapping("/project")
    public ResponseEntity<String> createProject(@RequestBody ProjectRequest request) {
        return service.createProject(request);
    }

    @GetMapping("/users/principal/projects")
    public Set<ProjectDto> getPrincipalsProjects() {
        return service.getPrincipalsProjects();
    }
}
