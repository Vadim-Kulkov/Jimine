package com.jimine.jiminebackend.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_info")
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "user_id")),
})
public class UserInfo extends BaseEntity {

    @OneToOne(mappedBy = "userInfo")
    private User user;
    @Column(name = "user_info_firstname")
    private String firstname;
    @Column(name = "user_info_lastname")
    private String lastname;
    @Column(name = "user_info_surname")
    private String surname;
    @Column(name = "user_sex")
    private String userSex;
}
