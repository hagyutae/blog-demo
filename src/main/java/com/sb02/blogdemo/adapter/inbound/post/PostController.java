package com.sb02.blogdemo.adapter.inbound.post;

import com.sb02.blogdemo.auth.RequiresAuth;
import com.sb02.blogdemo.core.posting.usecase.*;
import com.sb02.blogdemo.utils.TimeUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    @RequiresAuth
    public ResponseEntity<PublishPostResponse> publishPost(
            @RequestBody @Valid PublishPostRequest publishPostRequest,
            HttpServletRequest httpRequest
    ) {
        String userId = (String) httpRequest.getAttribute("userId");
        PublishPostCommand command = createPublishPostCommand(publishPostRequest, userId);
        PublishPostResult result = postService.publishPost(command);

        return ResponseEntity.ok(new PublishPostResponse(true, result.postId().toString()));
    }

    private PublishPostCommand createPublishPostCommand(PublishPostRequest publishPostRequest, String userId) {
        return new PublishPostCommand(
                publishPostRequest.title(),
                publishPostRequest.content(),
                userId,
                publishPostRequest.tags()
        );
    }

    @GetMapping("/{postId}")
    public ResponseEntity<RetrievedPost> retrieveById(@PathVariable UUID postId) {
        RetrievePostResult result = postService.retrievePostById(postId);
        return ResponseEntity.ok(convertToRetrievedPost(result));
    }

    private RetrievedPost convertToRetrievedPost(RetrievePostResult result) {
        return new RetrievedPost(
                result.postId().toString(),
                result.title(),
                result.content(),
                result.authorId(),
                result.authorNickname(),
                TimeUtils.formatedTimeString(result.createdAt()),
                TimeUtils.formatedTimeString(result.updatedAt()),
                result.tags()
        );
    }

    @GetMapping
    public ResponseEntity<RetrievePostsResponse> retrievePosts(
            @RequestParam(defaultValue = "0") long page,
            @RequestParam(defaultValue = "10") long size
    ) {
        RetrievePaginatedPostsCommand command = new RetrievePaginatedPostsCommand(page, size);
        RetrievePostsResult result = postService.retrievePaginatedPosts(command);

        return ResponseEntity.ok(convertToRetrievePostsResponse(result));
    }

    private RetrievePostsResponse convertToRetrievePostsResponse(RetrievePostsResult result) {
        return new RetrievePostsResponse(
                result.posts().stream().map(this::convertToRetrievedPost).toList(),
                result.totalPages(),
                result.currentPage()
        );
    }

    @PutMapping("/{postId}")
    @RequiresAuth
    public ResponseEntity<UpdatePostResponse> updatePost(
            @PathVariable UUID postId,
            @RequestBody @Valid UpdatePostRequest updatePostRequest,
            HttpServletRequest httpRequest
    ) {
        String userId = (String) httpRequest.getAttribute("userId");
        UpdatePostCommand command = createUpdatePostCommand(postId, userId, updatePostRequest);
        postService.updatePost(command);

        return ResponseEntity.ok(new UpdatePostResponse(true));
    }

    private UpdatePostCommand createUpdatePostCommand(UUID postId, String userId, UpdatePostRequest updatePostRequest) {
        return new UpdatePostCommand(
                userId,
                postId,
                updatePostRequest.title(),
                updatePostRequest.content(),
                updatePostRequest.tags()
        );
    }

    @DeleteMapping("/{postId}")
    @RequiresAuth
    public ResponseEntity<DeletePostResponse> deletePost(
            @PathVariable UUID postId,
            HttpServletRequest httpRequest
    ) {
        String userId = (String) httpRequest.getAttribute("userId");
        DeletePostCommand command = new DeletePostCommand(userId, postId);
        postService.deletePost(command);

        return ResponseEntity.ok(new DeletePostResponse(true));
    }
}
