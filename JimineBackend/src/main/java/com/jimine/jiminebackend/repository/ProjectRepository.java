package com.jimine.jiminebackend.repository;

import com.jimine.jiminebackend.model.Project;
import com.jimine.jiminebackend.model.reference.RefUserProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Set<Project> findAllByParticipantsIn(Set<RefUserProject> participants);
}
