package com.sb02.blogdemo.core.posting.usecase;


import com.sb02.blogdemo.adapter.outbound.user.UserRepository;
import com.sb02.blogdemo.core.posting.entity.Post;
import com.sb02.blogdemo.core.posting.entity.PostImage;
import com.sb02.blogdemo.core.posting.exception.PostNotFound;
import com.sb02.blogdemo.core.posting.port.PostImageRepositoryPort;
import com.sb02.blogdemo.core.posting.port.PostRepositoryPort;
import com.sb02.blogdemo.core.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostImageService postImageService;
    private final PostRepositoryPort postRepository;
    private final PostImageRepositoryPort postImageRepository;
    private final UserRepository userRepository;

    @Override
    public PublishPostResult publishPost(PublishPostCommand command) {
        Post post = Post.create(command.title(), command.content(), command.authorId(), command.tags());
        List<PostImage> postImages = postImageService.parseImages(post.getId(), post.getContent());
        postRepository.save(post);
        postImageRepository.saveAll(postImages);
        return new PublishPostResult(post.getId());
    }

    @Override
    public RetrievePostResult retrievePostById(UUID postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFound("Post with id " + postId + " not found"));
        String authorNickname = userRepository.findById(post.getAuthorId()).map(User::getNickname).orElse("");

        return convertToRetrievePostResult(post, authorNickname);
    }

    @Override
    public RetrievePostsResult retrievePaginatedPosts(RetrievePaginatedPostsCommand command) {
        List<Post> posts = postRepository.findAll(command.page(), command.size(), true);
        List<RetrievePostResult> results = posts.stream()
                .map(post -> {
                    String authorNickname = userRepository.findById(post.getAuthorId()).map(User::getNickname).orElse("");
                    return convertToRetrievePostResult(post, authorNickname);
                })
                .toList();

        int totalPages = (int) Math.ceil((double) postRepository.countAll() / (double) command.size());

        return new RetrievePostsResult(results, command.page(), command.size(), totalPages);
    }

    private RetrievePostResult convertToRetrievePostResult(Post post, String authorNickname) {
        return new RetrievePostResult(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getTags(),
                post.getAuthorId(),
                authorNickname,
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}
