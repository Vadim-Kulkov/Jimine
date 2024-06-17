package com.jimine.jiminebackend.model.request;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserProfileRequest {

    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private String surname;
    private String userSex;
}
