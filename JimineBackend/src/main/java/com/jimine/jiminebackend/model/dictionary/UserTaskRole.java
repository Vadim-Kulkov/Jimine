package com.jimine.jiminebackend.model.dictionary;

import jakarta.persistence.*;

@Entity
@Table
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "user_task_role_id")),
        @AttributeOverride(name = "name", column = @Column(name = "user_task_role_name"))
})
public class UserTaskRole extends BaseDictionary {
}
