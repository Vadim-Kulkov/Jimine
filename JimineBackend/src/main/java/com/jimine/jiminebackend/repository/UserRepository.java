package com.jimine.jiminebackend.repository;

import com.jimine.jiminebackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsernameOrEmail(String username, String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    @Query(value = """
            SELECT u
            FROM User u
            JOIN FETCH RefUserProject p
            WHERE
                p.project.id = :projectId
            """)
    List<User> findAllByProjectId(Long projectId);


    @Query(value = """
            SELECT u
            FROM User u
            JOIN FETCH RefUserProject p
            WHERE
                p.project.id = :projectId
                AND p.userProjectRole.id = :roleId
            """)
    List<User> findAllByProjectAndRoleIds(Long projectId, Long roleId);
}
