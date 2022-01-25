package com.example.forumapp.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostView {
    private Long id;
    private String name;
    private String description;
    private Long postGroupId;
    private String creator;
    private Instant createdDate;
    private Integer numberOfComments;
}
