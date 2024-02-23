package com.jimine.jiminebackend.model;

import jakarta.persistence.*;

@Entity
@Table
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "task_comment_id")),
})
public class TaskComment extends BaseEntity {
}
