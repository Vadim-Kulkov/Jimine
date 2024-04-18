package com.jimine.jiminebackend.model.reference.ckey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CKeyUserProject implements Serializable {

    @Column(name = "user_id")
    private Long userId;
    @Column(name = "project_id")
    private Long projectId;
    @Column(name = "user_project_role_id")
    private Long userProjectRoleId;
}
