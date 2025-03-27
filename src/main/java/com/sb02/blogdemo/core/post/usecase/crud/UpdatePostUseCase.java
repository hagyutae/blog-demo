package com.sb02.blogdemo.core.post.usecase.crud;

import com.sb02.blogdemo.core.post.usecase.crud.dto.UpdatePostCommand;

public interface UpdatePostUseCase {
    void updatePost(UpdatePostCommand command);
}
