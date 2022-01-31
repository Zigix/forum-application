package com.example.forumapp.service;

import com.example.forumapp.domain.dto.CreatePostGroupRequest;
import com.example.forumapp.domain.dto.PostGroupView;
import com.example.forumapp.domain.exception.PostGroupNameExistsException;
import com.example.forumapp.domain.exception.PostGroupNotFoundException;
import com.example.forumapp.domain.mapper.PostGroupMapper;
import com.example.forumapp.domain.model.PostGroup;
import com.example.forumapp.repository.PostGroupRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostGroupService {
    private final AuthService authService;
    private final PostGroupRepository postGroupRepository;
    private final PostGroupMapper postGroupMapper;

    @Async
    @Transactional(readOnly = true)
    public CompletableFuture<List<PostGroupView>> getAll() {
        log.info("getAll() method has been invoked");
        return CompletableFuture.completedFuture(postGroupRepository.findAll()
                .stream()
                .map(postGroupMapper::toPostGroupView)
                .toList());
    }

    @Async
    @Transactional(readOnly = true)
    public CompletableFuture<PostGroupView> get(Long id) {
        log.info("get() method has been invoked");
        PostGroup postGroup = postGroupRepository.findById(id)
                .orElseThrow(() ->
                        new PostGroupNotFoundException("PostGroup with id " + id + " not found"));
        return CompletableFuture.completedFuture(postGroupMapper.toPostGroupView(postGroup));
    }

    @Transactional
    public CompletableFuture<PostGroupView> create(CreatePostGroupRequest request) {
        if (postGroupRepository.findByName(request.getName()).isPresent()) {
            throw new PostGroupNameExistsException("PostGroup with name " + request.getName() + " already exists");
        }
        PostGroup postGroup = postGroupMapper.toPostGroup(request, authService.getLoggedUser());
        PostGroup save = postGroupRepository.save(postGroup);
        return CompletableFuture.completedFuture(postGroupMapper.toPostGroupView(save));
    }
}
