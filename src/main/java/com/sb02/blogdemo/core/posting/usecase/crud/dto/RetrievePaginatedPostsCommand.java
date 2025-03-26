package com.sb02.blogdemo.core.posting.usecase.crud.dto;

public record RetrievePaginatedPostsCommand(
    long page,
    long size
) {
}
