package com.jimine.jiminebackend.model.dictionary;

import jakarta.persistence.*;

@Entity
@Table
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "task_status_id")),
        @AttributeOverride(name = "name", column = @Column(name = "task_status_name"))
})
public class TaskStatus extends BaseDictionary {
}
