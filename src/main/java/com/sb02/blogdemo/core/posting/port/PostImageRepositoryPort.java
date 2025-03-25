package com.sb02.blogdemo.core.posting.port;

import com.sb02.blogdemo.core.posting.entity.PostImage;

import java.util.List;
import java.util.UUID;

public interface PostImageRepositoryPort {
    void save(PostImage postImage);
    void saveAll(List<PostImage> postImages);
    List<PostImage> retrieveImages(UUID postId);
    void delete(UUID postImageId);
}
