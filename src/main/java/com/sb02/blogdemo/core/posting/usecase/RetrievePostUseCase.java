package com.sb02.blogdemo.core.posting.usecase;

import java.util.UUID;

public interface RetrievePostUseCase {
    RetrievePostResult retrievePostById(UUID postId);
    RetrievePostsResult retrievePaginatedPosts(RetrievePaginatedPostsCommand command);
}
