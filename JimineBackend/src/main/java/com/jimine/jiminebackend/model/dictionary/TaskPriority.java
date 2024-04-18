package com.jimine.jiminebackend.model.dictionary;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "task_priority_id")),
        @AttributeOverride(name = "name", column = @Column(name = "task_priority_name"))
})
public class TaskPriority extends BaseDictionary {
}
