package com.sb02.blogdemo.core.posting.usecase.crud;

import com.sb02.blogdemo.core.posting.usecase.crud.dto.DeletePostCommand;

public interface DeletePostUseCase {
    void deletePost(DeletePostCommand command);
}
