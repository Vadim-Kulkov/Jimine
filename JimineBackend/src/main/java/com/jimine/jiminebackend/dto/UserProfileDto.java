package com.jimine.jiminebackend.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserProfileDto {

    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private String surname;
    private String userSex;
}
