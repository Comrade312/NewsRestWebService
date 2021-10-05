package com.example.demo.exception.request;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class NotEnoughRightsException extends RuntimeException {
    public NotEnoughRightsException(String message) {
        super(message);
    }
}
