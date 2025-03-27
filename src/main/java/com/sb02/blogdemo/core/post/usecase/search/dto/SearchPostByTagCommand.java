package com.sb02.blogdemo.core.post.usecase.search.dto;

public record SearchPostByTagCommand(
        String tag,
        long page,
        long size
) {
}
