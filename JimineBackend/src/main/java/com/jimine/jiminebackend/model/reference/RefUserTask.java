package com.jimine.jiminebackend.model.reference;

import com.jimine.jiminebackend.model.Task;
import com.jimine.jiminebackend.model.User;
import com.jimine.jiminebackend.model.dictionary.UserTaskRole;
import com.jimine.jiminebackend.model.reference.ckey.CKeyUserTask;
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
