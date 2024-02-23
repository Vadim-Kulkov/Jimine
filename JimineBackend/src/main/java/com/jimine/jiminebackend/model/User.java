package com.jimine.jiminebackend.model;

import jakarta.persistence.*;

@Entity
@Table
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "user_id")),
})
public class User extends BaseEntity {
}
