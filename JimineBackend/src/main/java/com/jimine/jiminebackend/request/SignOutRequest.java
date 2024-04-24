package com.jimine.jiminebackend.request;

import lombok.Data;

@Data
public class SignOutRequest {

    private String usernameOrEmail;
    private String password;
}
