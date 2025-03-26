package com.sb02.blogdemo.adapter.inbound.post.dto;

public record PublishPostResponse(
        boolean success,
        String postId
) {
}
