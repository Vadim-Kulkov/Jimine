package com.jimine.jiminebackend.model.request;

import lombok.Data;

@Data
public class SignOutRequest {

    private String usernameOrEmail;
    private String password;
}
