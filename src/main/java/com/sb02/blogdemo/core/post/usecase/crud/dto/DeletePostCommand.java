package com.sb02.blogdemo.core.post.usecase.crud.dto;

import java.util.UUID;

public record DeletePostCommand(
        String requestUserId,
        UUID postId
) {
}
