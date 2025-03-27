package com.sb02.blogdemo.core.post.usecase.crud.dto;

public record RetrievePaginatedPostsCommand(
    long page,
    long size
) {
}
