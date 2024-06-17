package com.jimine.jiminebackend.repository.dictionary;

import com.jimine.jiminebackend.model.entity.dictionary.ProjectStatus;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectStatusRepository extends JpaRepository<ProjectStatus, Long> {

    @NonNull
    Page<ProjectStatus> findAll(@NonNull Pageable pageable);
}
