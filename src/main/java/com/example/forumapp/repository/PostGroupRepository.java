package com.example.forumapp.repository;

import com.example.forumapp.domain.model.PostGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostGroupRepository extends JpaRepository<PostGroup, Long> {
    Optional<PostGroup> findByName(String name);
}
