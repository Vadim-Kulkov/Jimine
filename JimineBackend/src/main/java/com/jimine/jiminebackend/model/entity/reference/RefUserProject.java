package com.jimine.jiminebackend.model.entity.reference;

import com.jimine.jiminebackend.model.entity.Project;
import com.jimine.jiminebackend.model.entity.User;
import com.jimine.jiminebackend.model.entity.dictionary.UserProjectRole;
import com.jimine.jiminebackend.model.entity.reference.ckey.CKeyUserProject;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_project")
public class RefUserProject extends BaseReference {

    @EmbeddedId
    CKeyUserProject id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("projectId")
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @MapsId("userProjectRoleId")
    @JoinColumn(name = "user_project_role_id")
    private UserProjectRole userProjectRole;
}
