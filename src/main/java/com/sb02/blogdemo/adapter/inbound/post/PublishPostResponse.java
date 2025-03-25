package com.sb02.blogdemo.adapter.inbound.post;

public record PublishPostResponse(
        boolean success,
        String postId
) {
}
