package com.sb02.blogdemo.core.post.usecase.crud;

import com.sb02.blogdemo.core.post.usecase.dto.RetrievePostResult;
import com.sb02.blogdemo.core.post.usecase.crud.dto.RetrievePaginatedPostsCommand;
import com.sb02.blogdemo.core.post.usecase.dto.RetrievePostsResult;

import java.util.UUID;

public interface RetrievePostUseCase {
    RetrievePostResult retrievePostById(UUID postId);
    RetrievePostsResult retrievePaginatedPosts(RetrievePaginatedPostsCommand command);
}
