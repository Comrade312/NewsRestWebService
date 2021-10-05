package com.example.demo.controller;

import com.example.demo.facade.AuthFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.demo.dto.RegistrationRequestProto.RegistrationRequestDto;

/**
 * Controller for registration
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthFacade authFacade;

    /**
     * Controller for POST request to register user
     *
     * @param user {@link RegistrationRequestDto} object, contains username and password
     */
    @PostMapping(value = "/register")
    public ResponseEntity<Void> registration(@RequestBody RegistrationRequestDto user) {
        authFacade.registration(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}