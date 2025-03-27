package com.sb02.blogdemo.core.post.usecase.crud;

import com.sb02.blogdemo.core.post.usecase.crud.dto.DeletePostCommand;

public interface DeletePostUseCase {
    void deletePost(DeletePostCommand command);
}
