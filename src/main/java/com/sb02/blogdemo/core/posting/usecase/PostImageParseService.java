package com.sb02.blogdemo.core.posting.usecase;

import com.sb02.blogdemo.core.posting.entity.PostImage;

import java.util.List;
import java.util.UUID;

public interface PostImageParseService {
    List<PostImage> parseImages(UUID postId, String content);
}
