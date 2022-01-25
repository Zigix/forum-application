package com.example.forumapp.domain.mapper;

import com.example.forumapp.domain.dto.CreatePostRequest;
import com.example.forumapp.domain.dto.PostView;
import com.example.forumapp.domain.model.Comment;
import com.example.forumapp.domain.model.Post;
import com.example.forumapp.domain.model.PostGroup;
import com.example.forumapp.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface PostMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "request.name")
    @Mapping(target = "description", source = "request.description")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "postGroup", source = "postGroup")
    @Mapping(target = "comments", ignore = true)
    Post toPost(CreatePostRequest request, PostGroup postGroup, User user);

    @Mapping(target = "postGroupId", source = "post.postGroup.id")
    @Mapping(target = "creator", source = "post.user.username")
    @Mapping(target = "numberOfComments", expression = "java(getNumberOfComments(post.getComments()))")
    PostView toPostView(Post post);

    default Integer getNumberOfComments(List<Comment> comments) {
        if (comments == null) {
            return 0;
        }
        return comments.size();
    }
}
