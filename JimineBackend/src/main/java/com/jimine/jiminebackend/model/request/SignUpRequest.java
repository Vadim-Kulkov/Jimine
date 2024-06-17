package com.jimine.jiminebackend.model.request;

import com.jimine.jiminebackend.enums.RoleEnum;
import lombok.Data;

import java.util.Set;

@Data
public class SignUpRequest {

    private String name;
    private String username;
    private String email;
    private String password;
    private Set<RoleEnum> roles;
}
