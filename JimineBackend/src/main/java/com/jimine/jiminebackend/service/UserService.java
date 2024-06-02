package com.jimine.jiminebackend.service;

import com.jimine.jiminebackend.dto.UserDto;
import com.jimine.jiminebackend.dto.UserTaskDto;
import com.jimine.jiminebackend.model.BaseEntity;
import com.jimine.jiminebackend.model.Project;
import com.jimine.jiminebackend.model.User;
import com.jimine.jiminebackend.model.dictionary.UserProjectRole;
import com.jimine.jiminebackend.model.reference.RefUserProject;
import com.jimine.jiminebackend.model.reference.RefUserTask;
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

    private final EntityManager em;
    private final UserRepository repository;
    private final RefUserProjectRepository refUserProjectRepository;

    public User save(User user) {
        return repository.save(user);
    }

    public User create(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("User with given username param already exists");
        }
        if (repository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("User with given email param already exists");
        }
        return save(user);
    }

    public User getByUsername(String username) {
        return repository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    public List<UserTaskDto> getUsersByTaskId(Long taskId) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<UserTaskDto> criteriaQuery = criteriaBuilder.createQuery(UserTaskDto.class);
        Root<User> root = criteriaQuery.from(User.class);

        Join<User, RefUserTask> refUserTaskJoin = root.join("tasks", JoinType.INNER);
        criteriaQuery.where(
                List.of(
                        criteriaBuilder.equal(refUserTaskJoin.get("task").get("id"), taskId),
                        criteriaBuilder.isNull(refUserTaskJoin.get("task").get("deletedAt"))
                ).toArray(Predicate[]::new)
        );// todo userTaskRole should not be ADMIN

        criteriaQuery.select(
                criteriaBuilder.construct(
                        UserTaskDto.class,
                        root.get("id"),
                        root.get("username"),
                        refUserTaskJoin.get("userTaskRole").get("id"),
                        refUserTaskJoin.get("userTaskRole").get("name")
                )
        );
        return em.createQuery(criteriaQuery).getResultList();
    }

    public List<UserTaskDto> getUsersByProjectId(Long projectId) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<UserTaskDto> criteriaQuery = criteriaBuilder.createQuery(UserTaskDto.class);
        Root<User> root = criteriaQuery.from(User.class);

        Join<User, RefUserProject> refUserProjectJoin = root.join("projects", JoinType.INNER);
        criteriaQuery.where(
                List.of(
                        criteriaBuilder.equal(refUserProjectJoin.get("project").get("id"), projectId),
                        criteriaBuilder.isNull(refUserProjectJoin.get("project").get("deletedAt"))
                ).toArray(Predicate[]::new)
        ); // todo userProjectRole should not be ADMIN

        criteriaQuery.select(
                criteriaBuilder.construct(
                        UserTaskDto.class,
                        root.get("id"),
                        root.get("username"),
                        refUserProjectJoin.get("userProjectRole").get("id"),
                        refUserProjectJoin.get("userProjectRole").get("name")
                )
        );
        return em.createQuery(criteriaQuery).getResultList();
    }

    public List<UserDto> getUserPage(UserSearchRequest request) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
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
            Join<User, RefUserTask> refUserTaskJoin = root.join("tasks", JoinType.INNER);
            predicates.add(criteriaBuilder.equal(refUserTaskJoin.get("task").get("id"), request.getTaskId()));
        }

        criteriaQuery.where(predicates.toArray(Predicate[]::new));

        if (request.getPageSize() == null) {
            request.setPageSize(BasePageRequest.DEFAULT_PAGE_SIZE);
        }
        Set<User> users = new HashSet<>(
                em.createQuery((criteriaQuery))
                        .setFirstResult(0)
                        .setMaxResults(request.getPageSize())
                        .getResultList()
        );

        // todo добавить вывод дтохи с проектом и ролью на проекте
        List<UserDto> resultedDtos = users.stream().map(elem -> {
            return UserDto.builder()
                    .id(elem.getId())
                    .username(elem.getUsername())
                    .email(elem.getEmail())
                    .createdAt(elem.getCreatedAt())
                    .updatedAt(elem.getUpdatedAt())
                    .deletedAt(elem.getDeletedAt())
                    .userInfoId(elem.getUserInfo() != null ? elem.getUserInfo().getId() : null)
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