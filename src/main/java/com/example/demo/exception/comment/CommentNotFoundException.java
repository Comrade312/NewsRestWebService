package com.example.demo.exception.comment;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException() {
        super("News not found");
    }

    public CommentNotFoundException(Long id) {
        super("Can't find comment with id = " + id);
    }
}