package com.jimine.jiminebackend.model.entity.reference.ckey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class CKeyUserProject implements Serializable {

    @Column(name = "user_id")
    private Long userId;
    @Column(name = "project_id")
    private Long projectId;
    @Column(name = "user_project_role_id")
    private Long userProjectRoleId;
}
