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
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.NoPermissionException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final AuthService authService;
    private final PostGroupRepository postGroupRepository;
    private final PostRepository postRepository;
    private final PostMapper postMapper;

    @Transactional(readOnly = true)
    public PostView get(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() ->
                        new PostNotFoundException("Post with id " + id + " not found"));
        return postMapper.toPostView(post);
    }

    @Transactional(readOnly = true)
    public List<PostView> getByGroup(Long id) {
        return postRepository.findAllByPostGroupId(id)
                .stream()
                .map(postMapper::toPostView)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PostView> getByUser(String username) {
        return postRepository.findAllByUserUsername(username)
                .stream()
                .map(postMapper::toPostView)
                .toList();
    }

    public void create(CreatePostRequest request) {
        PostGroup postGroup = postGroupRepository
                .findById(request.getPostGroupId())
                .orElseThrow(() ->
                        new PostGroupNotFoundException("PostGroup with id " + request.getPostGroupId() + " not found"));
        User user = authService.getLoggedUser();
        Post post = postMapper.toPost(request, postGroup, user);
        postRepository.save(post);
    }

    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() ->
                        new PostGroupNotFoundException("Post with id " + id + " not found"));
        User user = authService.getLoggedUser();
        if (!user.equals(post.getUser())) {
            throw new IllegalStateException("No permission to delete this post");
        }
        postRepository.delete(post);
    }
}

//TODO: add cascade