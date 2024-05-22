package com.jimine.jiminebackend.model;

import com.jimine.jiminebackend.model.dictionary.TaskPriority;
import com.jimine.jiminebackend.model.dictionary.TaskStatus;
import com.jimine.jiminebackend.model.dictionary.TaskType;
import com.jimine.jiminebackend.model.reference.RefUserTask;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "task")
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "task_id")),
})
public class Task extends BaseEntity {

    @Column(name = "task_name")
    private String name;
    @Column(name = "task_description")
    private String description;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
    @ManyToOne
    @JoinColumn(name = "task_status_id")
    private TaskStatus taskStatus;
    @ManyToOne
    @JoinColumn(name = "task_type_id")
    private TaskType taskType;
    @ManyToOne
    @JoinColumn(name = "task_priority_id")
    private TaskPriority taskPriority;
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;
    @OneToMany(mappedBy = "task")
    private Set<TaskComment> comments;
    @OneToMany(mappedBy = "task")
    private Set<RefUserTask> workers;
}
