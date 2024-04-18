package com.jimine.jiminebackend.model.reference.ckey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class CKeyUserTask implements Serializable {

    @Column(name = "user_id")
    private Long userId;
    @Column(name = "task_id")
    private Long taskId;
    @Column(name = "user_task_role_id")
    private Long userTaskRoleId;
}
