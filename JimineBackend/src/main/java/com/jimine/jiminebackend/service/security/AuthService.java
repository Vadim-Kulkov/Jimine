package com.jimine.jiminebackend.service.security;

import com.jimine.jiminebackend.model.entity.User;
import com.jimine.jiminebackend.model.entity.dictionary.Role;
import com.jimine.jiminebackend.repository.RoleRepository;
import com.jimine.jiminebackend.repository.UserRepository;
import com.jimine.jiminebackend.model.request.SignInRequest;
import com.jimine.jiminebackend.model.request.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collections;


@RequiredArgsConstructor
@Service
public class AuthService {

    private final String SUCCESS_SIGN_IN_RESPONSE =  "User signed-in successfully!";
    private final String SUCCESS_REGISTRATION_RESPONSE = "User registered successfully!";
    private final String ERROR_USERNAME_IS_TAKEN = "Username is already taken!";
    private final String ERROR_EMAIL_IS_TAKEN = "Email is already taken!";
    private final String DEFAULT_USER_ROLE = "USER";

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<String> authenticateUser(@RequestBody SignInRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new ResponseEntity<>(SUCCESS_SIGN_IN_RESPONSE, HttpStatus.OK);
    }

    public ResponseEntity<String> registerUser(SignUpRequest signUpRequest){
        if(userRepository.existsByUsername(signUpRequest.getUsername())){
            return new ResponseEntity<>(ERROR_USERNAME_IS_TAKEN, HttpStatus.BAD_REQUEST);
        }
        if(userRepository.existsByEmail(signUpRequest.getEmail())){
            return new ResponseEntity<>(ERROR_EMAIL_IS_TAKEN, HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        Role userDefaultRole = roleRepository.findByName(DEFAULT_USER_ROLE)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setRoles(Collections.singleton(userDefaultRole));

        userRepository.save(user);
        return new ResponseEntity<>(SUCCESS_REGISTRATION_RESPONSE, HttpStatus.OK);
    }
}
