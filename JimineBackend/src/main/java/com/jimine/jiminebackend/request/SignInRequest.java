package com.jimine.jiminebackend.request;

import lombok.Data;

@Data
public class SignInRequest {

    private String usernameOrEmail;
    private String password;
}
