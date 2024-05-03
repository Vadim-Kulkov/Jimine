package com.jimine.jiminebackend.controller;

import com.jimine.jiminebackend.dto.UserProfileDto;
import com.jimine.jiminebackend.request.UserProfileRequest;
import com.jimine.jiminebackend.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserProfileController {

    private final UserProfileService service;

    @GetMapping("/users/principal/profile")
    public UserProfileDto getPrincipalProfile() {
        return service.getPrincipalProfile();
    }

    @PutMapping("/users/principal/profile")
    public ResponseEntity<String> updateProfile(@RequestBody UserProfileRequest request) {
        return service.updateProfile(request);
    }
}