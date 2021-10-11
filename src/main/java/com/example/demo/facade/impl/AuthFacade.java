package com.example.demo.facade.impl;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.facade.AuthBasicFacade;
import com.example.demo.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static com.example.demo.dto.RegistrationRequestProto.RegistrationRequestDto;

/**
 * Provides authorization api
 */
@Service
public class AuthFacade implements AuthBasicFacade {
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthFacade(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registration(RegistrationRequestDto request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Collections.singleton(Role.SUBSCRIBER));
        user.setActive(true);
        userService.save(user);
    }
}
