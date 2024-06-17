package com.jimine.jiminebackend.service.security;

import com.jimine.jiminebackend.model.dto.JwtAuthenticationResponse;
import com.jimine.jiminebackend.enums.RoleEnum;
import com.jimine.jiminebackend.model.entity.User;
import com.jimine.jiminebackend.model.entity.dictionary.Role;
import com.jimine.jiminebackend.repository.RoleRepository;
import com.jimine.jiminebackend.model.request.SignInRequest;
import com.jimine.jiminebackend.model.request.SignUpRequest;
import com.jimine.jiminebackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    /**
     * Регистрация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponse signUp(SignUpRequest request) {
        Role defaultRole = roleRepository.findByName(RoleEnum.ROLE_USER.getRoleName()).get();
        Set<GrantedAuthority> authorities = Set.of(defaultRole).stream().map((role)
                    -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());

//        com.jimine.jiminebackend.model.entity.User user = (com.jimine.jiminebackend.model.entity.User)User.builder()
//                .username(request.getUsername())
////                .email(request.getEmail()) todo shitted
//                .password(passwordEncoder.encode(request.getPassword()))
//                .authorities(authorities)
//                .build();

//        UserDetails userDetails = new User(request.getUsername(), request.getEmail(), authorities);

        User user = new User();
        user.setRoles(Set.of(defaultRole));
        user.setPassword(request.getPassword());
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
                userService.create(user);

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

    /**
     * Аутентификация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsernameOrEmail(), // todo getUsername
                request.getPassword()
        ));

        var user = userService
                .userDetailsService()
                .loadUserByUsername(request.getUsernameOrEmail());  // todo getUsername

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }
}