package com.jimine.jiminebackend.model.dictionary;

import jakarta.persistence.*;

@Entity
@Table
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "user_project_role_id")),
        @AttributeOverride(name = "name", column = @Column(name = "user_project_role_name"))
})
public class UserProjectRole extends BaseDictionary {
}
