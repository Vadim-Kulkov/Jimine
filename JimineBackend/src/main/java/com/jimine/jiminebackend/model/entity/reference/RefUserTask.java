package com.jimine.jiminebackend.model.entity.reference;

import com.jimine.jiminebackend.model.entity.Task;
import com.jimine.jiminebackend.model.entity.User;
import com.jimine.jiminebackend.model.entity.dictionary.UserTaskRole;
import com.jimine.jiminebackend.model.entity.reference.ckey.CKeyUserTask;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_task")
public class RefUserTask extends BaseReference {

    @EmbeddedId
    private CKeyUserTask id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @MapsId("taskId")
    @JoinColumn(name = "task_id")
    private Task task;
    @ManyToOne
    @MapsId("userTaskRoleId")
    @JoinColumn(name = "user_task_role_id")
    private UserTaskRole userTaskRole;
}
