package com.sb02.blogdemo.core.posting.usecase.crud;

import com.sb02.blogdemo.core.posting.usecase.crud.dto.UpdatePostCommand;

public interface UpdatePostUseCase {
    void updatePost(UpdatePostCommand command);
}
