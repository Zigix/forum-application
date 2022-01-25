package com.example.forumapp.api;

import com.example.forumapp.domain.dto.CreatePostRequest;
import com.example.forumapp.domain.dto.PostView;
import com.example.forumapp.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostApi {
    private final PostService postService;

    @GetMapping("/{id}")
    public ResponseEntity<PostView> get(@PathVariable Long id) {
        return ResponseEntity.ok(postService.get(id));
    }

    @GetMapping("/by-post-group/{id}")
    public ResponseEntity<List<PostView>> getByGroup(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getByGroup(id));
    }

    @GetMapping("/by-user/{username}")
    public ResponseEntity<List<PostView>> getByUser(@PathVariable String username) {
        return ResponseEntity.ok(postService.getByUser(username));
    }

    @PostMapping
    public ResponseEntity<Void> create(CreatePostRequest request) {
        postService.create(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        postService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
