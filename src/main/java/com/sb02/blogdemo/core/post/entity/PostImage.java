package com.sb02.blogdemo.core.post.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Getter
public class PostImage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;
    private UUID postId;
    private UUID imageId;

    private PostImage(UUID postId, UUID imageId) {
        this.id = UUID.randomUUID();
        this.postId = postId;
        this.imageId = imageId;
    }

    public static PostImage of(UUID postId, UUID imageId) {
        return new PostImage(postId, imageId);
    }
}
