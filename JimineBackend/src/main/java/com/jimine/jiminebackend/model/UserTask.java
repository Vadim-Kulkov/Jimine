package com.jimine.jiminebackend.model;

import jakarta.persistence.*;

@Entity
@Table
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "user_task_id")),
})
public class UserTask extends BaseEntity{
}
