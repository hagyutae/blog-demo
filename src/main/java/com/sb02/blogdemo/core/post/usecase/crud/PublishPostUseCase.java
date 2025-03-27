package com.sb02.blogdemo.core.post.usecase.crud;

import com.sb02.blogdemo.core.post.usecase.crud.dto.PublishPostCommand;
import com.sb02.blogdemo.core.post.usecase.crud.dto.PublishPostResult;

public interface PublishPostUseCase {
    PublishPostResult publishPost(PublishPostCommand command);
}
