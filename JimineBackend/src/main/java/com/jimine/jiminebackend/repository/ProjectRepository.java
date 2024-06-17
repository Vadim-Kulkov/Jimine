package com.jimine.jiminebackend.repository;

import com.jimine.jiminebackend.model.entity.Project;
import com.jimine.jiminebackend.model.entity.reference.RefUserProject;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Set<Project> findAllByParticipantsIn(Set<RefUserProject> participants);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(
            value = """ 
                    WITH delete_project AS (
                        UPDATE project 
                        SET deleted_at = now()
                        WHERE project_id = :projectId
                        RETURNING project_id
                    )
                    UPDATE task 
                    SET deleted_at = now()
                    WHERE project_id IN (SELECT project_id FROM delete_project)
                    """,
            nativeQuery = true
    )
    void deleteProjectById(Long projectId); // todo rename to mark as deleted
}
