package com.jimine.jiminebackend.service;

import com.jimine.jiminebackend.model.dto.TaskDto;
import com.jimine.jiminebackend.model.dto.TaskHistDto;
import com.jimine.jiminebackend.model.entity.Project;
import com.jimine.jiminebackend.model.entity.Task;
import com.jimine.jiminebackend.model.entity.User;
import com.jimine.jiminebackend.model.entity.dictionary.TaskPriority;
import com.jimine.jiminebackend.model.entity.dictionary.TaskStatus;
import com.jimine.jiminebackend.model.entity.dictionary.TaskType;
import com.jimine.jiminebackend.model.entity.dictionary.UserTaskRole;
import com.jimine.jiminebackend.model.entity.reference.RefUserTask;
import com.jimine.jiminebackend.model.entity.reference.ckey.CKeyUserTask;
import com.jimine.jiminebackend.repository.ProjectRepository;
import com.jimine.jiminebackend.repository.TaskRepository;
import com.jimine.jiminebackend.repository.dictionary.TaskPriorityRepository;
import com.jimine.jiminebackend.repository.dictionary.TaskStatusRepository;
import com.jimine.jiminebackend.repository.dictionary.TaskTypeRepository;
import com.jimine.jiminebackend.repository.reference.RefUserTaskRepository;
import com.jimine.jiminebackend.model.request.TaskWorkerRequest;
import com.jimine.jiminebackend.model.request.task.CreateTaskRequest;
import com.jimine.jiminebackend.model.request.task.UpdateTaskRequest;
import com.jimine.jiminebackend.service.security.SecurityService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
public class TaskService {

    private final EntityManager entityManager;

    private final TaskRepository taskRepository;
    private final TaskStatusRepository taskStatusRepository;
    private final TaskTypeRepository taskTypeRepository;
    private final TaskPriorityRepository taskPriorityRepository;
    private final ProjectRepository projectRepository;
    private final RefUserTaskRepository userTaskRepository;
    private final UserService userService;

    public List<TaskDto> findAllByProjectId(Long projectId, Map<String, String> searchParams) {
        if (projectId == null) {
            throw new RuntimeException("There's no projectId param");
        }
        searchParams.put("projectId", projectId.toString());
        return findAll(searchParams);
    }

    public List<TaskDto> findAllByPrincipal(Map<String, String> searchParams) {
        searchParams.put("workerId", SecurityService.getPrincipalUser().getId().toString());
        return findAll(searchParams);
    }

    public List<TaskDto> findAll(Map<String, String> searchParams) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TaskDto> criteriaQuery = criteriaBuilder.createQuery(TaskDto.class);
        Root<Task> taskRoot = criteriaQuery.from(Task.class);

        List<Predicate> predicates = new ArrayList<>();
        if (searchParams.containsKey("id")) {
            predicates.add(criteriaBuilder.equal(taskRoot.get("id"), searchParams.get("id")));
        }
        if (searchParams.containsKey("name") && !searchParams.get("name").isBlank()) { // check case-insensitivity
            predicates.add(criteriaBuilder.like(taskRoot.get("name"), '%' + searchParams.get("name") + '%')
            );
        }
        if (searchParams.containsKey("taskStatusId")) {
            predicates.add(criteriaBuilder.equal(
                    taskRoot.get("taskStatus").get("id"), searchParams.get("taskStatusId"))
            );
        }
        if (searchParams.containsKey("taskTypeId")) {
            predicates.add(criteriaBuilder.equal(taskRoot.get("taskType").get("id"), searchParams.get("taskTypeId")));
        }
        if (searchParams.containsKey("taskPriorityId")) {
            predicates.add(criteriaBuilder.equal(
                    taskRoot.get("taskPriority").get("id"), searchParams.get("taskPriorityId"))
            );
        }
        if (searchParams.containsKey("projectId")) {
            predicates.add(criteriaBuilder.equal(taskRoot.get("project").get("id"), searchParams.get("projectId")));
        }
        Join<Task, RefUserTask> taskUserRefJoin = taskRoot.join("workers");
        if (searchParams.containsKey("workerId")) {
            predicates.add(criteriaBuilder.equal(taskUserRefJoin.get("user").get("id"), searchParams.get("workerId")));
        }
//        if (searchParams.containsKey("deleted")) {
//            if (Boolean.valueOf(searchParams.get("deleted"))) {
//                predicates.add(criteriaBuilder.isNotNull(taskRoot.get("deletedAt")));
//            } else {
//                predicates.add(criteriaBuilder.isNull(taskRoot.get("deletedAt")));
//            }
//        } else {
//            predicates.add(criteriaBuilder.isNull(taskRoot.get("deletedAt")));
//        }

        criteriaQuery.where(predicates.toArray(Predicate[]::new));
        criteriaQuery.multiselect(
                criteriaBuilder.construct(
                        TaskDto.class,
                        taskRoot.get("id"),
                        taskRoot.get("name"),
                        taskRoot.get("description"),
                        taskRoot.get("taskStatus").get("id"),
                        taskRoot.get("taskStatus").get("name"),
                        taskRoot.get("taskType").get("id"),
                        taskRoot.get("taskType").get("name"),
                        taskRoot.get("taskPriority").get("id"),
                        taskRoot.get("taskPriority").get("name")
                )
        );
        List<TaskDto> resultDtos = entityManager.createQuery(criteriaQuery).getResultList();

        if (searchParams.containsKey("withUsers")) {
            resultDtos = resultDtos.stream().peek(
                    (elem) -> elem.setTaskWorkers(userService.getUsersByTaskId(elem.getTaskId()))
            ).toList();
        }
        return resultDtos;
    }

    public ResponseEntity<String> createTask(CreateTaskRequest request) {
        if (request.getProjectId() == null) {
            throw new RuntimeException("There's no projectId param");
        }
        if (request.getName() == null || request.getName().isBlank()) {
            throw new RuntimeException("There's no name param");
        }
        TaskStatus taskStatus = null;
        if (request.getTaskStatusId() != null) {
            taskStatus = taskStatusRepository.findById(request.getTaskStatusId())
                    .orElseThrow(() -> new RuntimeException("There's no taskStatus with given taskStatusId"));
        }
        TaskType taskType = null;
        if (request.getTaskTypeId() != null) {
            taskType = taskTypeRepository.findById(request.getTaskTypeId())
                    .orElseThrow(() -> new RuntimeException("There's no taskType with given taskTypeId"));
        }
        TaskPriority taskPriority = null;
        if (request.getTaskPriorityId() != null) {
            taskPriority = taskPriorityRepository.findById(request.getTaskPriorityId())
                    .orElseThrow(() -> new RuntimeException("There's no taskPriority with given taskPriorityId"));
        }

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("There's no project with this projectId"));

        Task taskToSave = Task.builder()
                .name(request.getName())
                .description(request.getDescription())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .taskStatus(taskStatus)
                .taskType(taskType)
                .taskPriority(taskPriority)
                .project(project).build();


        long taskId = taskRepository.save(taskToSave).getId();

        addWorkersToTheTask(Set.of(TaskWorkerRequest.builder()
                .workerId(SecurityService.getPrincipalUser().getId())
                .userTaskRoleId(0L).build() // todo magic value
        ), taskId);

        if (request.getWorkerRequestSet() != null && !request.getWorkerRequestSet().isEmpty()) {
            addWorkersToTheTask(new HashSet<>(request.getWorkerRequestSet()), taskId);
        }

        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    // todo should be list and role-check/check the PKey error
    public ResponseEntity<String> addWorkersToTheTask(Set<TaskWorkerRequest> workerRequestSet, Long taskId) {
        List<RefUserTask> refsToSave = workerRequestSet.stream().map(elem -> {
            CKeyUserTask cKeyUserTask = CKeyUserTask.builder()
                    .taskId(taskId)
                    .userId(elem.getWorkerId())
                    .userTaskRoleId(elem.getUserTaskRoleId())
                    .build();
            Task task = new Task();
            task.setId(taskId);
            User user = new User();
            user.setId(elem.getWorkerId());
            UserTaskRole userTaskRole = new UserTaskRole();
            userTaskRole.setId(elem.getUserTaskRoleId());
            return RefUserTask.builder()
                    .id(cKeyUserTask)
                    .task(task)
                    .user(user)
                    .userTaskRole(userTaskRole)
                    .build();
        }).toList();
        userTaskRepository.saveAll(refsToSave);
        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    public ResponseEntity<String> removeWorkerAssociationTheTask(Long taskId, Long roleId, Long userId) {
        if (taskId == null) {
            throw new RuntimeException("There's no taskId param");
        }
        if (userId == null) {
            throw new RuntimeException("There's no userId param");
        }
        if (roleId == null) {
            throw new RuntimeException("There's no roleId param");
        }
        CKeyUserTask userTaskCKey = CKeyUserTask.builder()
                .taskId(taskId)
                .userId(userId)
                .userTaskRoleId(roleId)
                .build();
        userTaskRepository.deleteById(userTaskCKey);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> deleteById(Long taskId) {
        Task task = entityManager.find(Task.class, taskId);
        task.setDeletedAt(LocalDateTime.now());
        entityManager.merge(task);
        // todo think about also marking worker-refs as deleted
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<String> updateTask(UpdateTaskRequest request, Long taskId) {
        Task task = entityManager.find(Task.class, taskId);
        task.setUpdatedAt(LocalDateTime.now());
        if (request.getName() != null && !request.getName().isBlank()) {
            task.setName(request.getName());
        }
        if (request.getDescription() != null && !request.getDescription().isBlank()) {
            task.setDescription(request.getDescription());
        }
        if (request.getTaskStatusId() != null) {
            TaskStatus status = taskStatusRepository.findById(request.getTaskStatusId()).orElseThrow(()
                    -> new RuntimeException("There's no taskStatus with this id param"));
            task.setTaskStatus(status);
        }
        if (request.getTaskTypeId() != null) {
            TaskType type = taskTypeRepository.findById(request.getTaskTypeId()).orElseThrow(()
                    -> new RuntimeException("There's no taskType with this id param"));
            task.setTaskType(type);
        }
        if (request.getTaskPriorityId() != null) {
            TaskPriority priority = taskPriorityRepository.findById(request.getTaskPriorityId()).orElseThrow(()
                    -> new RuntimeException("There's no taskPriority with this id param"));
            task.setTaskPriority(priority);
        }
        entityManager.persist(task);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    public List<TaskHistDto> getTaskHistory() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TaskHistDto> criteriaQuery = criteriaBuilder.createQuery(TaskHistDto.class);
        Root<Task> taskRoot = criteriaQuery.from(Task.class);

        List<Predicate> predicates = new ArrayList<>();

        Join<Task, RefUserTask> taskUserRefJoin = taskRoot.join("workers");

        predicates.add(criteriaBuilder.equal(
                taskUserRefJoin.get("user").get("id"), SecurityService.getPrincipalUser().getId())
        );
        predicates.add(criteriaBuilder.isNotNull(taskRoot.get("deletedAt")));

        criteriaQuery.where(predicates.toArray(Predicate[]::new));
        criteriaQuery.select(
                criteriaBuilder.construct(  // добавить роль юзера на задаче
                        TaskHistDto.class,
                        taskRoot.get("id"),
                        taskRoot.get("name"),
                        taskRoot.get("description"),
                        taskRoot.get("project").get("id"),
                        taskRoot.get("project").get("name"),
                        taskRoot.get("taskStatus").get("id"),
                        taskRoot.get("taskStatus").get("name"),
                        taskRoot.get("taskType").get("id"),
                        taskRoot.get("taskType").get("name"),
                        taskRoot.get("taskPriority").get("id"),
                        taskRoot.get("taskPriority").get("name"),
                        taskRoot.get("deletedAt")
                )
        );
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
