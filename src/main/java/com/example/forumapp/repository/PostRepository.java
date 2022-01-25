package com.example.forumapp.repository;

import com.example.forumapp.domain.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByPostGroupId(Long id);
    List<Post> findAllByUserUsername(String username);
}
