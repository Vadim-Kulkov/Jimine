package com.jimine.jiminebackend.repository.dictionary;

import com.jimine.jiminebackend.model.entity.dictionary.UserTaskRole;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTaskRoleRepository extends JpaRepository<UserTaskRole, Long> {

    @NonNull
    Page<UserTaskRole> findAll(@NonNull Pageable pageable);
}
