package com.example.forumapp.domain.exception;

public class PostGroupNameExistsException extends RuntimeException {
    public PostGroupNameExistsException(String message) {
        super(message);
    }
}
