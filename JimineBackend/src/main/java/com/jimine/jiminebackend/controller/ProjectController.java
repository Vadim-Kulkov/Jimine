package com.jimine.jiminebackend.controller;

import com.jimine.jiminebackend.model.dto.ProjectDto;
import com.jimine.jiminebackend.model.request.project.ProjectRequest;
import com.jimine.jiminebackend.model.request.project.UpdateProjectRequest;
import com.jimine.jiminebackend.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequiredArgsConstructor
@RestController
public class ProjectController {

    private final ProjectService service;

    @PostMapping("/projects")
    public ResponseEntity<String> createProject(@RequestBody ProjectRequest request) {
        return service.createProject(request);
    }

    @GetMapping("/users/principal/projects")
    public Set<ProjectDto> getPrincipalsProjects() {
        return service.getPrincipalsProjects();
    }

    @DeleteMapping("/projects/{projectId}")
    public ResponseEntity<String> deleteProject(@PathVariable Long projectId) {
        return service.deleteProjectById(projectId);
    }

    @PutMapping("/projects/{projectId}")
    public ResponseEntity<String> updateProject(
            @RequestBody UpdateProjectRequest request,
            @PathVariable Long projectId) {
        return service.updateProjectById(request, projectId);
    }
}
