package com.jimine.jiminebackend.controller;

import com.jimine.jiminebackend.dto.ProjectDto;
import com.jimine.jiminebackend.request.project.ProjectRequest;
import com.jimine.jiminebackend.request.project.UpdateProjectRequest;
import com.jimine.jiminebackend.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequiredArgsConstructor
@RestController
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

    @DeleteMapping("/project/{projectId}")
    public ResponseEntity<String> deleteProject(@PathVariable Long projectId) {
        return service.deleteProjectById(projectId);
    }

    @PutMapping("/project/{projectId}")
    public ResponseEntity<String> updateProject(UpdateProjectRequest request, Long projectId) {
        return service.updateProjectById(request, projectId);
    }
}
