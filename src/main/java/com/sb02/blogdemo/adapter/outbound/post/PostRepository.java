package com.sb02.blogdemo.adapter.outbound.post;

import com.sb02.blogdemo.adapter.outbound.SimpleFileRepository;
import com.sb02.blogdemo.core.posting.entity.Post;
import com.sb02.blogdemo.core.posting.port.PostRepositoryPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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

    @Override
    public Optional<Post> findById(UUID postId) {
        return Optional.ofNullable(data.get(postId));
    }

    @Override
    public List<Post> findAll() {
        return data.values().stream().toList();
    }

    @Override
    public List<Post> findAll(long page, long size, boolean newestFirst) {
        return data.values().stream()
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .skip(page * size)
                .limit(size)
                .toList();
    }

    @Override
    public int countAll() {
        return data.size();
    }

    @Override
    public void delete(UUID postId) {
        data.remove(postId);
        saveData();
    }
}
