package com.jimine.jiminebackend.repository.reference;

import com.jimine.jiminebackend.model.reference.RefUserTask;
import com.jimine.jiminebackend.model.reference.ckey.CKeyUserTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefUserTaskRepository extends JpaRepository<RefUserTask, CKeyUserTask> {
}
