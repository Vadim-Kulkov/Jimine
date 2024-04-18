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
        @AttributeOverride(name = "id", column = @Column(name = "project_status_id")),
        @AttributeOverride(name = "name", column = @Column(name = "project_status_name"))
})
public class ProjectStatus extends BaseDictionary {
}
