package com.jimine.jiminebackend.model.entity.dictionary;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "task_priority")
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "task_priority_id")),
        @AttributeOverride(name = "name", column = @Column(name = "task_priority_name"))
})
public class TaskPriority extends BaseDictionary {
}
