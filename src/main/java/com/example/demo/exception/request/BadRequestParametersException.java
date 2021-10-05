package com.example.demo.exception.request;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestParametersException extends RuntimeException {
    public BadRequestParametersException(String message) {
        super(message);
    }
}