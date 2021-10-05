package com.example.demo.exception.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException() {
        super("Employee not found");
    }

    public UserNotFoundException(Long id) {
        super("Can't find employee with id = " + id);
    }
}