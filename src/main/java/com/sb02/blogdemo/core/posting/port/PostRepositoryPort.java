package com.sb02.blogdemo.core.posting.port;

import com.sb02.blogdemo.core.posting.entity.Post;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PostRepositoryPort {

    void save(Post post);
    Optional<Post> findById(UUID postId);
    List<Post> findAll(long page, long size, boolean newestFirst);
    int countAll();
    void delete(UUID postId);
}
