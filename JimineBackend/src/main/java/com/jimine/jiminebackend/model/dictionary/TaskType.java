package com.jimine.jiminebackend.model.dictionary;

import jakarta.persistence.*;

@Entity
@Table
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "task_type_id")),
        @AttributeOverride(name = "name", column = @Column(name = "task_type_name"))
})
public class TaskType extends BaseDictionary {
}
