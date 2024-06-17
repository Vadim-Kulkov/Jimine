package com.jimine.jiminebackend.model.request;

import lombok.Data;

@Data
public class SignInRequest {

    private String usernameOrEmail;
    private String password;
}
