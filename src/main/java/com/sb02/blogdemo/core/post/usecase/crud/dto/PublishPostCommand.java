package com.sb02.blogdemo.core.post.usecase.crud.dto;

import java.util.List;

public record PublishPostCommand(
        String title,
        String content,
        String authorId,
        List<String> tags
) {
}
