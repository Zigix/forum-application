package com.example.forumapp.repository;

import com.example.forumapp.domain.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPostId(Long id);
    List<Comment> findAllByUserUsername(String username);
}
