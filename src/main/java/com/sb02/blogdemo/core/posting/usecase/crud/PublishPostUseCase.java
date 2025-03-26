package com.sb02.blogdemo.core.posting.usecase.crud;

import com.sb02.blogdemo.core.posting.usecase.crud.dto.PublishPostCommand;
import com.sb02.blogdemo.core.posting.usecase.crud.dto.PublishPostResult;

public interface PublishPostUseCase {
    PublishPostResult publishPost(PublishPostCommand command);
}
