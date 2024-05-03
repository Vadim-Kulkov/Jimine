package com.jimine.jiminebackend.service;

import com.jimine.jiminebackend.dto.UserDto;
import com.jimine.jiminebackend.model.BaseEntity;
import com.jimine.jiminebackend.model.Project;
import com.jimine.jiminebackend.model.User;
import com.jimine.jiminebackend.model.dictionary.UserProjectRole;
import com.jimine.jiminebackend.model.reference.RefUserProject;
import com.jimine.jiminebackend.model.reference.ckey.CKeyUserProject;
import com.jimine.jiminebackend.repository.UserRepository;
import com.jimine.jiminebackend.repository.reference.RefUserProjectRepository;
import com.jimine.jiminebackend.request.BasePageRequest;
import com.jimine.jiminebackend.request.user.UserSearchRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final EntityManager entityManager;
    private final UserRepository repository;
    private final RefUserProjectRepository refUserProjectRepository;

    public User save(User user) {
        return repository.save(user);
    }

    public User create(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            // Заменить на свои исключения
            throw new RuntimeException("Пользователь с таким именем уже существует");
        }

        if (repository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }

        return save(user);
    }

    public User getByUsername(String username) {
        return repository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    public User getCurrentUser() { // todo сделать статиком и юзать в других местах
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }

    public List<UserDto> getUserPage(UserSearchRequest request) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);

        Root<User> root = criteriaQuery.from(User.class);

        List<Predicate> predicates = new ArrayList<>();
        if (request.getUsername() != null && !request.getUsername().isBlank()) {
            predicates.add(criteriaBuilder.equal(root.get("username"), request.getUsername()));
        }
        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            predicates.add(criteriaBuilder.equal(root.get("email"), request.getEmail()));
        }
        if (request.getProjectId() != null) {
            Join<User, RefUserProject> userProjectRefJoin = root.join("projects", JoinType.INNER);
            predicates.add(criteriaBuilder.equal(root.get("projects").get("project").get("id"), request.getProjectId()));
            if (request.getRoleId() != null) {
                predicates.add(criteriaBuilder
                        .equal(userProjectRefJoin.get("userProjectRole").get("id"), request.getRoleId()));
            }

        }
        if (request.getTaskId() != null) {

        }

        criteriaQuery.where(predicates.toArray(Predicate[]::new));

        if (request.getPageSize() == null) {
            request.setPageSize(BasePageRequest.defaultPageSize);
        }
        Set<User> users = new HashSet<>(
                entityManager.createQuery((criteriaQuery))
                        .setFirstResult(0)
                        .setMaxResults(request.getPageSize())
                        .getResultList()
        );

        List<UserDto> resultedDtos = users.stream().map(elem -> {
            return UserDto.builder()
                    .username(elem.getUsername())
                    .email(elem.getEmail())
                    .createdAt(elem.getCreatedAt())
                    .updatedAt(elem.getUpdatedAt())
                    .deletedAt(elem.getDeletedAt())
                    .userInfoId(elem.getUserInfo().getId())
                    .commentIds(elem.getComments().stream().map(BaseEntity::getId).collect(Collectors.toSet()))
                    .roleIds(elem.getRoles().stream().map(BaseEntity::getId).collect(Collectors.toSet()))
                    .projectIds(elem.getProjects().stream().map(e -> e.getProject().getId()).collect(Collectors.toSet()))
//                    .taskIds(elem.getTasks().stream().map(e -> e.getTask().getId()).collect(Collectors.toSet()))
                    .build();
        }).toList();

        return resultedDtos;
    }

    public List<UserDto> findUsersByProjectId(UserSearchRequest request) {
        if(request.getProjectId() == null) {
            throw new RuntimeException("There's no projectId param");
        }
        List<User> resultedUsers;
        if(request.getRoleId() != null) {
            resultedUsers = repository.findAllByProjectAndRoleIds(request.getProjectId(), request.getRoleId());
        }
        resultedUsers = repository.findAllByProjectId(request.getProjectId());
        return resultedUsers.stream().map(elem -> {
            return UserDto.builder()
                    .username(elem.getUsername())
                    .email(elem.getEmail())
                    .createdAt(elem.getCreatedAt())
                    .updatedAt(elem.getUpdatedAt())
                    .deletedAt(elem.getDeletedAt())
                    .userInfoId(elem.getUserInfo().getId())
                    .commentIds(elem.getComments().stream().map(BaseEntity::getId).collect(Collectors.toSet()))
                    .roleIds(elem.getRoles().stream().map(BaseEntity::getId).collect(Collectors.toSet()))
                    .projectIds(elem.getProjects().stream().map(e -> e.getProject().getId()).collect(Collectors.toSet()))
                    .taskIds(elem.getTasks().stream().map(e -> e.getTask().getId()).collect(Collectors.toSet()))
                    .build();
        }).toList();
    }
    public ResponseEntity<String> addUserToTheProject(UserSearchRequest request) {
        if(request.getUserId() == null) {
            throw new RuntimeException("There's no userId param");
        }
        if(request.getProjectId() == null) {
            throw new RuntimeException("There's no projectId param");
        }
        if(request.getRoleId() == null) {
            throw new RuntimeException("There's no roleId param");
        }
        User user = new User();
        user.setId(request.getUserId());

        Project project = new Project();
        project.setId(request.getProjectId());

        UserProjectRole projectRole = new UserProjectRole();
        projectRole.setId(request.getRoleId());

        CKeyUserProject primaryRefKey = CKeyUserProject.builder()
                .projectId(request.getProjectId())
                .userId(request.getUserId())
                .userProjectRoleId(request.getRoleId())
                .build();

        RefUserProject refUserProject = RefUserProject.builder()
                .id(primaryRefKey)
                .user(user)
                .project(project)
                .userProjectRole(projectRole)
                .build();

        refUserProjectRepository.save(refUserProject);
        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }
}