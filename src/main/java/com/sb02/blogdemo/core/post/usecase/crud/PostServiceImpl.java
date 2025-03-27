package com.sb02.blogdemo.core.post.usecase.crud;

import com.sb02.blogdemo.core.image.usecase.DeleteImageUseCase;
import com.sb02.blogdemo.core.post.entity.Post;
import com.sb02.blogdemo.core.post.entity.PostImage;
import com.sb02.blogdemo.core.post.port.PostImageRepositoryPort;
import com.sb02.blogdemo.core.post.port.PostRepositoryPort;
import com.sb02.blogdemo.core.post.usecase.crud.dto.*;
import com.sb02.blogdemo.core.post.usecase.dto.RetrievePostResult;
import com.sb02.blogdemo.core.post.usecase.dto.RetrievePostsResult;
import com.sb02.blogdemo.core.user.entity.User;
import com.sb02.blogdemo.core.user.port.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

import static com.sb02.blogdemo.core.post.exception.PostErrors.invalidPostAccessError;
import static com.sb02.blogdemo.core.post.exception.PostErrors.postNotFoundError;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostImageParseService postImageParseService;
    private final PostRepositoryPort postRepository;
    private final PostImageRepositoryPort postImageRepository;
    private final UserRepositoryPort userRepository;
    private final DeleteImageUseCase deleteImageUseCase;

    @Override
    public PublishPostResult publishPost(PublishPostCommand command) {
        return Optional.of(command)
                .map(cmd -> Post.create(cmd.title(), cmd.content(), cmd.authorId(), cmd.tags()))
                .map(post -> {
                    List<PostImage> postImages = postImageParseService.parseImages(post.getId(), post.getContent());
                    postRepository.save(post);
                    postImageRepository.saveAll(postImages);
                    return new PublishPostResult(post.getId());
                })
                .get();
    }

    @Override
    public RetrievePostResult retrievePostById(UUID postId) {
        return postRepository.findById(postId)
                .map(post -> {
                    String nickname = findAuthorNickname(post.getAuthorId());
                    return toRetrievePostResult(post, nickname);
                })
                .orElseThrow(() -> postNotFoundError(postId));
    }

    @Override
    public RetrievePostsResult retrievePaginatedPosts(RetrievePaginatedPostsCommand command) {
        List<Post> posts = postRepository.findAll(command.page(), command.size(), true);

        return new RetrievePostsResult(
                posts.stream()
                        .map(post -> toRetrievePostResult(post, findAuthorNickname(post.getAuthorId())))
                        .toList(),
                command.page(),
                command.size(),
                calculateTotalPages(postRepository.countAll(), command.size())
        );
    }

    private int calculateTotalPages(int totalItems, long pageSize) {
        return (int) Math.ceil((double) totalItems / pageSize);
    }

    private String findAuthorNickname(String authorId) {
        return userRepository.findById(authorId)
                .map(User::getNickname)
                .orElse("");
    }

    private RetrievePostResult toRetrievePostResult(Post post, String authorNickname) {
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
        postRepository.findById(command.postId())
                .map(post -> validateAndUpdatePost(post, command))
                .map(this::updatePostImages)
                .orElseThrow(() -> postNotFoundError(command.postId()));
    }

    private Post validateAndUpdatePost(Post post, UpdatePostCommand command) {
        if (!post.getAuthorId().equals(command.requestUserId())) {
            throw invalidPostAccessError(command.requestUserId());
        }

        post.update(command.title(), command.content(), command.tags());
        postRepository.save(post);
        return post;
    }

    private Post updatePostImages(Post post) {
        List<PostImage> parsedImages = postImageParseService.parseImages(post.getId(), post.getContent());
        List<PostImage> existingImages = postImageRepository.retrieveImages(post.getId());

        processImageDifferences(parsedImages, existingImages);
        return post;
    }

    private void processImageDifferences(List<PostImage> newImages, List<PostImage> existingImages) {
        // Process images to delete (in existing but not in new)
        existingImages.stream()
                .filter(createImageToDeletePredicate(newImages))
                .forEach(this::deletePostImage);

        // Process images to add (in new but not in existing)
        newImages.stream()
                .filter(createNewImagePredicate(existingImages))
                .forEach(postImageRepository::save);
    }

    private Predicate<PostImage> createImageToDeletePredicate(List<PostImage> newImages) {
        return existingImage -> newImages.stream()
                .map(PostImage::getImageId)
                .noneMatch(existingImage.getImageId()::equals);
    }

    private Predicate<PostImage> createNewImagePredicate(List<PostImage> existingImages) {
        return newImage -> existingImages.stream()
                .map(PostImage::getImageId)
                .noneMatch(newImage.getImageId()::equals);
    }

    private void deletePostImage(PostImage postImage) {
        postImageRepository.delete(postImage.getId());
        deleteImageUseCase.deleteImage(postImage.getImageId());
    }

    @Override
    public void deletePost(DeletePostCommand command) {
        postRepository.findById(command.postId())
                .ifPresentOrElse(
                        post -> {
                            // Delete all associated images first
                            postImageRepository.retrieveImages(post.getId())
                                    .forEach(this::deletePostImage);
                            // Then delete the post
                            postRepository.delete(post.getId());
                        },
                        () -> { throw postNotFoundError(command.postId()); }
                );
    }
}