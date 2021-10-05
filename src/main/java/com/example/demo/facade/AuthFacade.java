package com.example.demo.facade;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

import static com.example.demo.dto.RegistrationRequestProto.RegistrationRequestDto;

/**
 * Provides authorization api
 */
@Service
public class AuthFacade {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Method for user registration. Create user with role SUBSCRIBER
     *
     * @param request {@link RegistrationRequestDto} object, contains username and password
     */
    public void registration(RegistrationRequestDto request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Collections.singleton(Role.SUBSCRIBER));
        user.setActive(true);
        userService.save(user);
    }
}
