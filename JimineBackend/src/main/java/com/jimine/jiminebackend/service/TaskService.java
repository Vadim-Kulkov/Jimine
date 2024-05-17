package com.jimine.jiminebackend.service;

import com.jimine.jiminebackend.dto.TaskDto;
import com.jimine.jiminebackend.model.Project;
import com.jimine.jiminebackend.model.Task;
import com.jimine.jiminebackend.model.User;
import com.jimine.jiminebackend.model.dictionary.TaskPriority;
import com.jimine.jiminebackend.model.dictionary.TaskStatus;
import com.jimine.jiminebackend.model.dictionary.TaskType;
import com.jimine.jiminebackend.model.dictionary.UserTaskRole;
import com.jimine.jiminebackend.model.reference.RefUserTask;
import com.jimine.jiminebackend.model.reference.ckey.CKeyUserTask;
import com.jimine.jiminebackend.repository.ProjectRepository;
import com.jimine.jiminebackend.repository.TaskRepository;
import com.jimine.jiminebackend.repository.dictionary.TaskPriorityRepository;
import com.jimine.jiminebackend.repository.dictionary.TaskStatusRepository;
import com.jimine.jiminebackend.repository.dictionary.TaskTypeRepository;
import com.jimine.jiminebackend.repository.reference.RefUserTaskRepository;
import com.jimine.jiminebackend.request.TaskWorkerRequest;
import com.jimine.jiminebackend.request.task.CreateTaskRequest;
import com.jimine.jiminebackend.request.task.TaskPageRequest;
import com.jimine.jiminebackend.request.task.UpdateTaskRequest;
import com.jimine.jiminebackend.service.security.SecurityService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    public List<TaskDto> getTaskPage(TaskPageRequest request) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TaskDto> criteriaQuery = criteriaBuilder.createQuery(TaskDto.class);
        Root<Task> taskRoot = criteriaQuery.from(Task.class);

        List<Predicate> predicates = new ArrayList<>();
        if (request.getTaskId() != null) {
            predicates.add(criteriaBuilder.equal(taskRoot.get("id"), request.getTaskId()));
        }
        if (request.getTaskName() != null && !request.getTaskName().isBlank()) { // check case-insensitivity
            predicates.add(criteriaBuilder.like(taskRoot.get("name"), "%" + request.getTaskName().toLowerCase() + "%"));
        }
        if (request.getTaskStatusId() != null) {
            predicates.add(criteriaBuilder.equal(taskRoot.get("taskStatus").get("id"), request.getTaskStatusId()));
        }
        if (request.getTaskTypeId() != null) {
            predicates.add(criteriaBuilder.equal(taskRoot.get("taskType").get("id"), request.getTaskTypeId()));
        }
        if (request.getTaskPriorityId() != null) {
            predicates.add(criteriaBuilder.equal(taskRoot.get("taskPriority").get("id"), request.getTaskPriorityId()));
        }
        if (request.getProjectId() != null) {
            predicates.add(criteriaBuilder.equal(taskRoot.get("project").get("id"), request.getProjectId()));
        }
        if (request.getWorkerId() != null) {
            Join<Task, RefUserTask> taskUserRefJoin = taskRoot.join("workers");
            predicates.add(criteriaBuilder.equal(taskUserRefJoin.get("user").get("id"), request.getWorkerId()));
        }
        //taskRoot.fetch("comments", JoinType.LEFT);
        predicates.add(criteriaBuilder.isNull(taskRoot.get("deletedAt")));
        criteriaQuery.where(predicates.toArray(Predicate[]::new));
        criteriaQuery.select(
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
        return entityManager.createQuery(criteriaQuery).getResultList();
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
        if(taskId == null) {
            throw new RuntimeException("There's no taskId param");
        }
        if(userId == null) {
            throw new RuntimeException("There's no userId param");
        }
        if(roleId == null) {
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

    @Transactional // todo remove
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
}
