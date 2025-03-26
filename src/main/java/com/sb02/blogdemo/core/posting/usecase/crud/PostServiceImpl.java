package com.sb02.blogdemo.core.posting.usecase.crud;


import com.sb02.blogdemo.adapter.outbound.user.UserRepository;
import com.sb02.blogdemo.core.image.usecase.ImageService;
import com.sb02.blogdemo.core.posting.entity.Post;
import com.sb02.blogdemo.core.posting.entity.PostImage;
import com.sb02.blogdemo.core.posting.exception.InvalidPostAccess;
import com.sb02.blogdemo.core.posting.exception.PostNotFound;
import com.sb02.blogdemo.core.posting.port.PostImageRepositoryPort;
import com.sb02.blogdemo.core.posting.port.PostRepositoryPort;
import com.sb02.blogdemo.core.posting.usecase.dto.RetrievePostResult;
import com.sb02.blogdemo.core.posting.usecase.dto.RetrievePostsResult;
import com.sb02.blogdemo.core.posting.usecase.crud.dto.*;
import com.sb02.blogdemo.core.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostImageParseService postImageParseService;
    private final PostRepositoryPort postRepository;
    private final PostImageRepositoryPort postImageRepository;
    private final UserRepository userRepository;
    private final ImageService imageService;

    @Override
    public PublishPostResult publishPost(PublishPostCommand command) {
        Post post = Post.create(command.title(), command.content(), command.authorId(), command.tags());
        List<PostImage> postImages = postImageParseService.parseImages(post.getId(), post.getContent());
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

    @Override
    public void updatePost(UpdatePostCommand command) {
        Post post = postRepository.findById(command.postId()).orElseThrow(() -> new PostNotFound("Post with id " + command.postId() + " not found"));

        String requestUserId = command.requestUserId();

        if (!post.getAuthorId().equals(requestUserId)) {
            throw new InvalidPostAccess("User with id " + requestUserId + " does not have access to modifying post with id " + command.postId());
        }

        post.update(command.title(), command.content(), command.tags());
        postRepository.save(post);

        List<PostImage> parsedPostImages = postImageParseService.parseImages(post.getId(), post.getContent());
        List<PostImage> existingPostImages = postImageRepository.retrieveImages(post.getId());

        processPostImages(parsedPostImages, existingPostImages);
    }

    private void processPostImages(List<PostImage> parsedPostImages, List<PostImage> existingPostImages) {
        // 삭제할 이미지 처리
        existingPostImages.stream()
                .filter(existingImage -> isImageToDelete(existingImage, parsedPostImages))
                .forEach(this::deletePostImage);

        // 새 이미지 저장
        parsedPostImages.stream()
                .filter(parsedImage -> isNewImage(parsedImage, existingPostImages))
                .forEach(postImageRepository::save);
    }

    private boolean isImageToDelete(PostImage existingImage, List<PostImage> parsedImages) {
        return parsedImages.stream()
                .map(PostImage::getImageId)
                .noneMatch(existingImage.getImageId()::equals);
    }

    private boolean isNewImage(PostImage parsedImage, List<PostImage> existingImages) {
        return existingImages.stream()
                .map(PostImage::getImageId)
                .noneMatch(parsedImage.getImageId()::equals);
    }

    private void deletePostImage(PostImage postImage) {
        postImageRepository.delete(postImage.getId());
        imageService.deleteImage(postImage.getImageId());
    }

    @Override
    public void deletePost(DeletePostCommand command) {
        Post post = postRepository.findById(command.postId()).orElseThrow(() -> new PostNotFound("Post with id " + command.postId() + " not found"));
        postImageRepository.retrieveImages(post.getId()).forEach(this::deletePostImage);
        postRepository.delete(post.getId());
    }
}
