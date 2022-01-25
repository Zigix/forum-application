package com.example.forumapp.domain.exception;

public class PostGroupNotFoundException extends RuntimeException {
    public PostGroupNotFoundException(String message) {
        super(message);
    }
}
