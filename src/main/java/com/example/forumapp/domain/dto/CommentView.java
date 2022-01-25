package com.example.forumapp.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentView {
    private Long id;
    private Long postId;
    private String text;
    private Instant createdDate;
    private String creator;
}
