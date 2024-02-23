package com.jimine.jiminebackend.model.dictionary;

import jakarta.persistence.*;

@Entity
@Table
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "task_priority_id")),
        @AttributeOverride(name = "name", column = @Column(name = "task_priority_name"))
})
public class TaskPriority extends BaseDictionary {
}
