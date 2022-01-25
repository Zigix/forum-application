package com.example.forumapp.api;

import com.example.forumapp.domain.dto.CreatePostGroupRequest;
import com.example.forumapp.domain.dto.PostGroupView;
import com.example.forumapp.service.PostGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post-groups")
@RequiredArgsConstructor
public class PostGroupApi {
    private final PostGroupService postGroupService;

    @GetMapping
    public ResponseEntity<List<PostGroupView>> getAll() {
        return ResponseEntity.ok(postGroupService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostGroupView> get(@PathVariable Long id) {
        return ResponseEntity.ok(postGroupService.get(id));
    }

    @PostMapping
    public ResponseEntity<PostGroupView> create(@RequestBody CreatePostGroupRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postGroupService.create(request));
    }
}
