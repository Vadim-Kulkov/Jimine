package com.jimine.jiminebackend.model.entity.dictionary;

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
        @AttributeOverride(name = "id", column = @Column(name = "user_task_role_id")),
        @AttributeOverride(name = "name", column = @Column(name = "user_task_role_name"))
})
public class UserTaskRole extends BaseDictionary {
}
