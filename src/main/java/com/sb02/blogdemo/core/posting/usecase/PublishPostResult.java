package com.sb02.blogdemo.core.posting.usecase;

import java.util.UUID;

public record PublishPostResult(
        UUID postId
) {
}
