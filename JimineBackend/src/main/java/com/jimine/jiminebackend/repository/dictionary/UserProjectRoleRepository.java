package com.jimine.jiminebackend.repository.dictionary;

import com.jimine.jiminebackend.model.dictionary.UserProjectRole;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProjectRoleRepository extends JpaRepository<UserProjectRole, Long> {

    @NonNull
    Page<UserProjectRole> findAll(@NonNull Pageable pageable);
}
