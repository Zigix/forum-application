package com.example.forumapp.domain.model;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "post_groups")
public class PostGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Instant createdDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "postGroup")
    private List<Post> posts;
}
