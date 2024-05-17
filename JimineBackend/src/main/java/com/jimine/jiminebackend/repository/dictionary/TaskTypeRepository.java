package com.jimine.jiminebackend.repository.dictionary;

import com.jimine.jiminebackend.model.dictionary.TaskType;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskTypeRepository extends JpaRepository<TaskType, Long> {

    @NonNull
    Page<TaskType> findAll(@NonNull Pageable pageable);
}
