package com.jimine.jiminebackend.repository.reference;

import com.jimine.jiminebackend.model.reference.RefUserProject;
import com.jimine.jiminebackend.model.reference.ckey.CKeyUserProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefUserProjectRepository extends JpaRepository<RefUserProject, CKeyUserProject> {

    RefUserProject findByUserId(Long userId);
}
