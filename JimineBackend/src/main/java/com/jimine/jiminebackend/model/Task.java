package com.jimine.jiminebackend.model;

import jakarta.persistence.*;

@Entity
@Table
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "task_id")),
})
public class Task extends BaseEntity {
}
