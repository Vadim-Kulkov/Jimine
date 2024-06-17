package com.jimine.jiminebackend.model.entity;

import com.jimine.jiminebackend.model.entity.dictionary.ProjectStatus;
import com.jimine.jiminebackend.model.entity.reference.RefUserProject;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "project")
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "project_id")),
})
public class Project extends BaseEntity {

    @Column(name = "project_name")
    private String name;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
    @Column(name = "project_description")
    private String description;
    @ManyToOne
    @JoinColumn(name = "project_status_id")
    private ProjectStatus projectStatus;
    @OneToMany(mappedBy = "project")
    private Set<RefUserProject> participants;
    @OneToMany(mappedBy = "project")
    private Set<Task> tasks;
}
