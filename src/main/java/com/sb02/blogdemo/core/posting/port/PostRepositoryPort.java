package com.sb02.blogdemo.core.posting.port;

import com.sb02.blogdemo.core.posting.entity.Post;
import com.sb02.blogdemo.core.posting.entity.PostImage;

import java.util.List;

public interface PostRepositoryPort {

    void save(Post post);
}
