package com.jimine.jiminebackend.model.dictionary;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "user_project_role")
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "user_project_role_id")),
        @AttributeOverride(name = "name", column = @Column(name = "user_project_role_name"))
})
public class UserProjectRole extends BaseDictionary {
}
