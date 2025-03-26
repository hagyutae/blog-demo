package com.sb02.blogdemo.adapter.inbound.post.dto;

import java.util.List;

public record RetrievedPost(
        String id,
        String title,
        String content,
        String authorId,
        String authorNickname,
        String createdAt,
        String updatedAt,
        List<String> tags
) {
}
