package com.sb02.blogdemo.core.post.usecase.search.dto;

public record SearchPostByKeywordCommand(
        String keyword,
        long page,
        long size
) {
}
