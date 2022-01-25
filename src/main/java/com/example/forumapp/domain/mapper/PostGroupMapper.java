package com.example.forumapp.domain.mapper;

import com.example.forumapp.domain.dto.CreatePostGroupRequest;
import com.example.forumapp.domain.dto.PostGroupView;
import com.example.forumapp.domain.model.Post;
import com.example.forumapp.domain.model.PostGroup;
import com.example.forumapp.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostGroupMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "request.name")
    @Mapping(target = "description", source = "request.description")
    @Mapping(target = "createdDate", expression = "java(java.time.Instant.now())")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "posts", ignore = true)
    PostGroup toPostGroup(CreatePostGroupRequest request, User user);

    @Mapping(target = "creator", expression = "java(postGroup.getUser().getUsername())")
    @Mapping(target = "numberOfPosts", expression = "java(getPostsSize(postGroup.getPosts()))")
    PostGroupView toPostGroupView(PostGroup postGroup);

    default Integer getPostsSize(List<Post> posts) {
        if (posts == null) {
            return 0;
        }
        return posts.size();
    }
}
