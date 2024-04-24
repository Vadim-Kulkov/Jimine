package com.jimine.jiminebackend.service;

import com.jimine.jiminebackend.dto.ProjectDto;
import com.jimine.jiminebackend.enums.ProjectStatusEnum;
import com.jimine.jiminebackend.enums.UserProjectRoleEnum;
import com.jimine.jiminebackend.model.Project;
import com.jimine.jiminebackend.model.User;
import com.jimine.jiminebackend.model.dictionary.ProjectStatus;
import com.jimine.jiminebackend.model.dictionary.UserProjectRole;
import com.jimine.jiminebackend.model.reference.RefUserProject;
import com.jimine.jiminebackend.model.reference.ckey.CKeyUserProject;
import com.jimine.jiminebackend.repository.ProjectRepository;
import com.jimine.jiminebackend.repository.reference.RefUserProjectRepository;
import com.jimine.jiminebackend.request.ProjectRequest;
import com.jimine.jiminebackend.service.security.SecurityService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import static com.jimine.jiminebackend.service.security.SecurityService.getPrincipalUser;

@RequiredArgsConstructor
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final RefUserProjectRepository refUserProjectRepository;

    @Transactional
    public ResponseEntity<String> createProject(ProjectRequest request) {
        if (request.getProjectName().isEmpty()) {
            throw new RuntimeException("There's no 'projectName' param!");
        }

        ProjectStatus defaultProjectStatus = new ProjectStatus();
        defaultProjectStatus.setId(ProjectStatusEnum.NEW.getProjectStatusId());

        Project project = Project.builder()
                .name(request.getProjectName())
                .description(request.getProjectDescription())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .projectStatus(defaultProjectStatus).build();

        project = projectRepository.save(project);
        User currentUser = getPrincipalUser();

        UserProjectRole userProjectRole = new UserProjectRole();
        userProjectRole.setId(UserProjectRoleEnum.ADMIN.getUserProjectRoleId());

        RefUserProject refUserProject = RefUserProject.builder()
                .id(new CKeyUserProject(
                                currentUser.getId(),
                                project.getId(),
                                UserProjectRoleEnum.ADMIN.getUserProjectRoleId()
                        )
                )
                .user(currentUser)
                .project(project)
                .userProjectRole(userProjectRole).build();

        refUserProjectRepository.save(refUserProject);

        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    public Set<ProjectDto> getPrincipalsProjects() {
        Set<RefUserProject> refUserProjects = refUserProjectRepository.findAllByUserIn(
                Set.of(SecurityService.getPrincipalUser())
        );

        return projectRepository
                .findAllByParticipantsIn(refUserProjects)
                .stream().map(
                        elem -> ProjectDto.builder()
                                .id(elem.getId())
                                .name(elem.getName())
                                .description(elem.getDescription())
                                .projectStatusName(elem.getProjectStatus().getName())
                                .build()
                ).collect(Collectors.toSet());
    }
}
