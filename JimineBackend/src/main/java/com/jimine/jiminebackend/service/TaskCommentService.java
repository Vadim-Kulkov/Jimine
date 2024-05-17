package com.jimine.jiminebackend.service;

import com.jimine.jiminebackend.dto.TaskCommentDto;
import com.jimine.jiminebackend.model.Task;
import com.jimine.jiminebackend.model.TaskComment;
import com.jimine.jiminebackend.repository.TaskCommentRepository;
import com.jimine.jiminebackend.repository.TaskRepository;
import com.jimine.jiminebackend.request.CreateTaskCommentRequest;
import com.jimine.jiminebackend.request.TaskCommentRequest;
import com.jimine.jiminebackend.service.security.SecurityService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskCommentService {

    private final EntityManager em;
    private final TaskCommentRepository taskCommentRepository;
    private final TaskRepository taskRepository;

    public List<TaskCommentDto> getPage(TaskCommentRequest request, Long taskId) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<TaskCommentDto> cq = cb.createQuery(TaskCommentDto.class);
        Root<TaskComment> commentRoot = cq.from(TaskComment.class);

        List<Predicate> predicates = new ArrayList<>();

        if (request.getCommentId() != null) {
            predicates.add(cb.equal(commentRoot.get("id"), request.getCommentId()));
        }
        if (request.getCommentName() != null && !request.getCommentName().isBlank()) {
            predicates.add(cb.like(commentRoot.get("name"), '%' + request.getCommentName() + '%'));
        }
        if (taskId == null) {
            throw new RuntimeException("There's no taskId param");
        }
        if (request.getUserId() != null) {
            predicates.add(cb.equal(commentRoot.get("creator").get("id"), request.getUserId()));
        }
        if(request.getPageStart() == null) {
            request.setPageStart(0);
        }
        predicates.add(cb.equal(commentRoot.get("task").get("id"), taskId));
        predicates.add(cb.isNull(commentRoot.get("deletedAt")));
        cq.where(predicates.toArray(Predicate[]::new));

        TypedQuery<TaskCommentDto> typedQuery = em.createQuery(cq.select(
                cb.construct(
                        TaskCommentDto.class,
                        commentRoot.get("id"),
                        commentRoot.get("name"),
                        commentRoot.get("content"),
                        commentRoot.get("createdAt"),
                        commentRoot.get("creator").get("id"),
                        commentRoot.get("creator").get("username")
                )
        ));
        if (request.getPageSize() == null || request.getPageSize() == 0) {
            request.setDefaultPageSize();
        }
        typedQuery.setFirstResult(request.getPageStart());
        typedQuery.setMaxResults(request.getPageSize());
        return typedQuery.getResultList();
    }

    public ResponseEntity<String> createTaskComment(CreateTaskCommentRequest request, Long taskId) {
        if(taskId == null) {
            throw new RuntimeException("There'is no taskId param");
        }
        if(request.getName() == null || request.getName().isBlank()) {
            throw new RuntimeException("There'is no name param");
        }
        Task task = taskRepository.findById(taskId).orElseThrow(()
                -> new RuntimeException("There's no task with given id param"));

        TaskComment commentToSave = TaskComment.builder()
                .name(request.getName())
                .content(request.getContent())
                .createdAt(LocalDateTime.now())
                .creator(SecurityService.getPrincipalUser())
                .task(task)
                .build();
        taskCommentRepository.save(commentToSave);
        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    @Transactional
    public ResponseEntity<String> updateTaskComment(CreateTaskCommentRequest request, Long commentId) {
        TaskComment taskComment = taskCommentRepository.findById(commentId).orElseThrow(() ->
                new RuntimeException("There's no comment with passed id param"));
        if(request.getName() != null && !request.getName().isBlank()) {
            taskComment.setName(request.getName());
        }
        if(request.getContent() != null && !request.getContent().isBlank()) {
            taskComment.setContent(request.getContent());
        }
        em.persist(taskComment);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
