package com.jimine.jiminebackend.repository.dictionary;

import com.jimine.jiminebackend.model.entity.dictionary.TaskStatus;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskStatusRepository extends JpaRepository<TaskStatus, Long> {

    @NonNull
    Page<TaskStatus> findAll(@NonNull Pageable pageable);
}
