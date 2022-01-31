package com.example.forumapp.api;

import com.example.forumapp.domain.dto.CommentView;
import com.example.forumapp.domain.dto.CreateCommentRequest;
import com.example.forumapp.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentApi {
    private final CommentService commentService;

    @GetMapping("/{id}")
    public ResponseEntity<CommentView> get(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.get(id).join());
    }

    @GetMapping("/by-post/{id}")
    public ResponseEntity<List<CommentView>> getByPost(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getByPost(id).join());
    }

    @GetMapping("/by-user/{username}")
    public ResponseEntity<List<CommentView>> getByUser(@PathVariable String username) {
        return ResponseEntity.ok(commentService.getByUser(username).join());
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CreateCommentRequest request) {
        commentService.create(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        commentService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
