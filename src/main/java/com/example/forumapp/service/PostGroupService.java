package com.example.forumapp.service;

import com.example.forumapp.domain.dto.CreatePostGroupRequest;
import com.example.forumapp.domain.dto.PostGroupView;
import com.example.forumapp.domain.exception.PostGroupNameExistsException;
import com.example.forumapp.domain.exception.PostGroupNotFoundException;
import com.example.forumapp.domain.mapper.PostGroupMapper;
import com.example.forumapp.domain.model.PostGroup;
import com.example.forumapp.repository.PostGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostGroupService {
    private final AuthService authService;
    private final PostGroupRepository postGroupRepository;
    private final PostGroupMapper postGroupMapper;

    @Transactional(readOnly = true)
    public List<PostGroupView> getAll() {
        return postGroupRepository.findAll()
                .stream()
                .map(postGroupMapper::toPostGroupView)
                .toList();
    }

    @Transactional(readOnly = true)
    public PostGroupView get(Long id) {
        PostGroup postGroup = postGroupRepository.findById(id)
                .orElseThrow(() ->
                        new PostGroupNotFoundException("PostGroup with id " + id + " not found"));
        return postGroupMapper.toPostGroupView(postGroup);
    }

    @Transactional
    public PostGroupView create(CreatePostGroupRequest request) {
        if (postGroupRepository.findByName(request.getName()).isPresent()) {
            throw new PostGroupNameExistsException("PostGroup with name " + request.getName() + " already exists");
        }
        PostGroup postGroup = postGroupMapper.toPostGroup(request, authService.getLoggedUser());
        PostGroup save = postGroupRepository.save(postGroup);
        return postGroupMapper.toPostGroupView(save);
    }
}
