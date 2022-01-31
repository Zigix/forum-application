package com.example.forumapp.service;

import com.example.forumapp.domain.dto.CommentView;
import com.example.forumapp.domain.dto.CreateCommentRequest;
import com.example.forumapp.domain.exception.CommentNotFoundException;
import com.example.forumapp.domain.exception.PostGroupNotFoundException;
import com.example.forumapp.domain.mapper.CommentMapper;
import com.example.forumapp.domain.model.Comment;
import com.example.forumapp.domain.model.Post;
import com.example.forumapp.domain.model.User;
import com.example.forumapp.repository.CommentRepository;
import com.example.forumapp.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final AuthService authService;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;

    @Async
    @Transactional(readOnly = true)
    public CompletableFuture<CommentView> get(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() ->
                        new CommentNotFoundException("Comment with id " + id + " not found"));
        return CompletableFuture.completedFuture(commentMapper.toCommentView(comment));
    }

    @Async
    @Transactional(readOnly = true)
    public CompletableFuture<List<CommentView>> getByPost(Long id) {
        return CompletableFuture.completedFuture(commentRepository.findAllByPostId(id)
                .stream()
                .map(commentMapper::toCommentView)
                .toList());
    }

    @Async
    @Transactional(readOnly = true)
    public CompletableFuture<List<CommentView>> getByUser(String username) {
        return CompletableFuture.completedFuture(commentRepository.findAllByUserUsername(username)
                .stream()
                .map(commentMapper::toCommentView)
                .toList());
    }

    @Transactional
    public void create(CreateCommentRequest request) {
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() ->
                        new PostGroupNotFoundException("Post with id " + request.getPostId() + " not found"));
        User user = authService.getLoggedUser();
        Comment comment = commentMapper.toComment(request, user, post);
        commentRepository.save(comment);
    }

    @Transactional
    public void delete(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment with id " + id + " not found"));
        String loggedUsername = authService.getLoggedUser().getUsername();
        String creatorUsername = comment.getUser().getUsername();
        if (!loggedUsername.equals(creatorUsername)) {
            throw new IllegalStateException("No permission to delete this post");
        }
        commentRepository.delete(comment);
    }
}
