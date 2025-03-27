package com.sb02.blogdemo.core.post.usecase.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record RetrievePostResult(
        UUID postId,
        String title,
        String content,
        List<String> tags,
        String authorId,
        String authorNickname,
        Instant createdAt,
        Instant updatedAt
) {
}
