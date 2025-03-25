package com.sb02.blogdemo.adapter.outbound.post;

import com.sb02.blogdemo.adapter.outbound.SimpleFileRepository;
import com.sb02.blogdemo.core.posting.entity.PostImage;
import com.sb02.blogdemo.core.posting.port.PostImageRepositoryPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class PostImageRepository extends SimpleFileRepository<UUID, PostImage> implements PostImageRepositoryPort {

    public PostImageRepository(@Value("${storage.path}") String storageDir) {
        super(storageDir, "post_image.ser");
    }

    @Override
    public void save(PostImage postImage) {
        data.put(postImage.getId(), postImage);
        saveData();
    }

    @Override
    public void saveAll(List<PostImage> postImages) {
        postImages.forEach(postImage -> data.put(postImage.getId(), postImage));
        saveData();
    }

    @Override
    public List<PostImage> retrieveImages(UUID postId) {
        return data.values().stream().filter(postImage -> postImage.getPostId().equals(postId)).toList();
    }

    @Override
    public void delete(UUID postImageId) {
        data.remove(postImageId);
        saveData();
    }
}
