package com.sb02.blogdemo.core.posting.usecase;

public record RetrievePaginatedPostsCommand(
    long page,
    long size
) {
}
