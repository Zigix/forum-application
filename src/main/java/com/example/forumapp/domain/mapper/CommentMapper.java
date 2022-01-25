package com.example.forumapp.domain.mapper;

import com.example.forumapp.domain.dto.CommentView;
import com.example.forumapp.domain.dto.CreateCommentRequest;
import com.example.forumapp.domain.model.Comment;
import com.example.forumapp.domain.model.Post;
import com.example.forumapp.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "postId", expression = "java(comment.getPost().getId())")
    @Mapping(target = "creator", expression = "java(comment.getUser().getUsername())")
    CommentView toCommentView(Comment comment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "text", source = "request.text")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "post", source = "post")
    Comment toComment(CreateCommentRequest request, User user, Post post);
}
