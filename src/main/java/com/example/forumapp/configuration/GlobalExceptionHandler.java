package com.example.forumapp.configuration;

import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {

    @EventListener(Exception.class)
    public String handleException(Exception e) {
        return e.getMessage();
    }
}

