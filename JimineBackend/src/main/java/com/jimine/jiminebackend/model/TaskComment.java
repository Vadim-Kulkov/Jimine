package com.jimine.jiminebackend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "task_comment")
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "task_comment_id")),
})
public class TaskComment extends BaseEntity {

    @Column(name = "task_comment_name")
    private String name;
    @Column(name = "task_comment_content")
    private String content;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
}
