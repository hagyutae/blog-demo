package com.sb02.blogdemo.core.post.usecase.crud;

import com.sb02.blogdemo.core.post.entity.PostImage;

import java.util.List;
import java.util.UUID;

public interface PostImageParseService {
    List<PostImage> parseImages(UUID postId, String content);
}
