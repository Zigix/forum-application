package com.example.forumapp.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserView {
    private Long id;
    private String email;
    private String username;
    private Instant createdDate;
    private boolean enabled;
}
