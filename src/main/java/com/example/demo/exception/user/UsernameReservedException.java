package com.example.demo.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UsernameReservedException extends RuntimeException {
    public UsernameReservedException(String username) {
        super("Error: Username is already taken! = " + username);
    }
}
