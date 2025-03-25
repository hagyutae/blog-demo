package com.sb02.blogdemo.adapter.outbound.post;

import com.sb02.blogdemo.adapter.outbound.SimpleFileRepository;
import com.sb02.blogdemo.core.posting.entity.Post;
import com.sb02.blogdemo.core.posting.port.PostRepositoryPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class PostRepository extends SimpleFileRepository<UUID, Post> implements PostRepositoryPort {

    public PostRepository(@Value("${storage.path}") String storageDir) {
        super(storageDir, "post.ser");
    }

    @Override
    public void save(Post post) {
        data.put(post.getId(), post);
        saveData();
    }
}
