package com.sb02.blogdemo.core.posting.port;

import com.sb02.blogdemo.core.posting.entity.PostImage;

import java.util.List;

public interface PostImageRepositoryPort {
    void saveAll(List<PostImage> postImages);
}
