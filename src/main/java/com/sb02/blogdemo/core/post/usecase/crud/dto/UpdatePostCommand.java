package com.sb02.blogdemo.core.post.usecase.crud.dto;

import java.util.List;
import java.util.UUID;

public record UpdatePostCommand(
        String requestUserId,
        UUID postId,
        String title,
        String content,
        List<String> tags
) {
}
