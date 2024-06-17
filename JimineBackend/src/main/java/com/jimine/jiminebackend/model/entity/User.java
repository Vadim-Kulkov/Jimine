package com.jimine.jiminebackend.model.entity;

import com.jimine.jiminebackend.model.entity.dictionary.Role;
import com.jimine.jiminebackend.model.entity.reference.RefUserProject;
import com.jimine.jiminebackend.model.entity.reference.RefUserTask;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_entity")
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "user_id")),
})
public class User extends BaseEntity implements UserDetails {

    @OneToMany(mappedBy = "creator")
    private Set<TaskComment> comments;
    @Column(name = "user_username")
    private String username;
    @Column(name = "user_email")
    private String email;
    @Column(name = "user_password")
    private String password;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
    @OneToOne
    @JoinColumn(name = "user_id")
    private UserInfo userInfo;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id"))
    private Set<Role> roles;
    @OneToMany(mappedBy = "user")
    private Set<RefUserProject> projects;
    @OneToMany(mappedBy = "user")
    private Set<RefUserTask> tasks;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(e -> new SimpleGrantedAuthority(e.getName())).collect(Collectors.toSet());
    }

    @Override
    public boolean isAccountNonExpired() {
        return deletedAt == null;
    }

    @Override
    public boolean isAccountNonLocked() {
        return deletedAt == null;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return deletedAt == null;
    }

    @Override
    public boolean isEnabled() {
        return deletedAt == null;
    }
}
