package com.jimine.jiminebackend.repository.dictionary;

import com.jimine.jiminebackend.model.entity.dictionary.TaskPriority;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskPriorityRepository extends JpaRepository<TaskPriority, Long> {

    @NonNull
    Page<TaskPriority> findAll(@NonNull Pageable pageable);
}
