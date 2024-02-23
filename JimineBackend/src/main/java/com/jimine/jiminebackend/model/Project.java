package com.jimine.jiminebackend.model;

import jakarta.persistence.*;

@Entity
@Table
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "preoject_id")),
})
public class Project extends BaseEntity {
}
