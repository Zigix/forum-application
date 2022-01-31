package com.example.forumapp.service;

import com.example.forumapp.domain.dto.CreatePostRequest;
import com.example.forumapp.domain.dto.PostView;
import com.example.forumapp.domain.exception.PostGroupNotFoundException;
import com.example.forumapp.domain.exception.PostNotFoundException;
import com.example.forumapp.domain.mapper.PostMapper;
import com.example.forumapp.domain.model.Post;
import com.example.forumapp.domain.model.PostGroup;
import com.example.forumapp.domain.model.User;
import com.example.forumapp.repository.PostGroupRepository;
import com.example.forumapp.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class PostService {
    private final AuthService authService;
    private final PostGroupRepository postGroupRepository;
    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Async
    @Transactional(readOnly = true)
    public CompletableFuture<PostView> get(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() ->
                        new PostNotFoundException("Post with id " + id + " not found"));
        return CompletableFuture.completedFuture(postMapper.toPostView(post));
    }

    @Async
    @Transactional(readOnly = true)
    public CompletableFuture<List<PostView>> getByGroup(Long id) {
        return CompletableFuture.completedFuture(postRepository.findAllByPostGroupId(id)
                .stream()
                .map(postMapper::toPostView)
                .toList());
    }

    @Async
    @Transactional(readOnly = true)
    public CompletableFuture<List<PostView>> getByUser(String username) {
        return CompletableFuture.completedFuture(postRepository.findAllByUserUsername(username)
                .stream()
                .map(postMapper::toPostView)
                .toList());
    }

    @Transactional
    public void create(CreatePostRequest request) {
        PostGroup postGroup = postGroupRepository
                .findById(request.getPostGroupId())
                .orElseThrow(() ->
                        new PostGroupNotFoundException("PostGroup with id " + request.getPostGroupId() + " not found"));
        User user = authService.getLoggedUser();
        Post post = postMapper.toPost(request, postGroup, user);
        postRepository.save(post);
    }

    @Transactional
    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() ->
                        new PostGroupNotFoundException("Post with id " + id + " not found"));
        String username = authService.getLoggedUser().getUsername();
        if (!username.equals(post.getUser().getUsername())) {
            throw new IllegalStateException("No permission to delete this post");
        }
        postRepository.delete(post);
    }
}
