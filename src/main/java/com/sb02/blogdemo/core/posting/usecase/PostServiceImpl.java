package com.sb02.blogdemo.core.posting.usecase;


import com.sb02.blogdemo.core.posting.entity.Post;
import com.sb02.blogdemo.core.posting.entity.PostImage;
import com.sb02.blogdemo.core.posting.port.PostImageRepositoryPort;
import com.sb02.blogdemo.core.posting.port.PostRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostImageService postImageService;
    private final PostRepositoryPort postRepository;
    private final PostImageRepositoryPort postImageRepository;

    @Override
    public PublishPostResult publishPost(PublishPostCommand command) {
        Post post = Post.create(command.title(), command.content(), command.authorId(), command.tags());
        List<PostImage> postImages = postImageService.parseImages(post.getId(), post.getContent());
        postRepository.save(post);
        postImageRepository.saveAll(postImages);
        return new PublishPostResult(post.getId());
    }
}
