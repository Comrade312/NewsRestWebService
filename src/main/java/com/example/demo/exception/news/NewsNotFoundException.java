package com.example.demo.exception.news;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NewsNotFoundException extends RuntimeException {
    public NewsNotFoundException() {
        super("News not found");
    }

    public NewsNotFoundException(Long id) {
        super("Can't find news with id = " + id);
    }
}