package com.sb02.blogdemo.adapter.inbound.post;

import com.sb02.blogdemo.adapter.inbound.post.dto.*;
import com.sb02.blogdemo.auth.RequiresAuth;
import com.sb02.blogdemo.core.posting.usecase.crud.PostService;
import com.sb02.blogdemo.core.posting.usecase.crud.dto.*;
import com.sb02.blogdemo.core.posting.usecase.dto.RetrievePostResult;
import com.sb02.blogdemo.core.posting.usecase.dto.RetrievePostsResult;
import com.sb02.blogdemo.core.posting.usecase.search.PostSearchService;
import com.sb02.blogdemo.core.posting.usecase.search.dto.SearchPostByKeywordCommand;
import com.sb02.blogdemo.core.posting.usecase.search.dto.SearchPostByTagCommand;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.sb02.blogdemo.adapter.inbound.post.PostDtoMapper.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostSearchService postSearchService;

    @PostMapping
    @RequiresAuth
    public ResponseEntity<PublishPostResponse> publishPost(
            @RequestBody @Valid PublishPostRequest publishPostRequest,
            HttpServletRequest httpRequest
    ) {
        var userId = (String) httpRequest.getAttribute("userId");
        PublishPostCommand command = toPublishPostCommand(publishPostRequest, userId);
        PublishPostResult result = postService.publishPost(command);

        return ResponseEntity.ok(toPublishPostResponse(result));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<RetrievedPost> retrieveById(@PathVariable UUID postId) {
        RetrievePostResult result = postService.retrievePostById(postId);

        return ResponseEntity.ok(toRetrievedPost(result));
    }

    @GetMapping
    public ResponseEntity<RetrievePostsResponse> retrievePosts(
            @RequestParam(defaultValue = "0") long page,
            @RequestParam(defaultValue = "10") long size
    ) {
        RetrievePaginatedPostsCommand command = new RetrievePaginatedPostsCommand(page, size);
        RetrievePostsResult result = postService.retrievePaginatedPosts(command);

        return ResponseEntity.ok(toRetrievePostsResponse(result));
    }

    @PutMapping("/{postId}")
    @RequiresAuth
    public ResponseEntity<UpdatePostResponse> updatePost(
            @PathVariable UUID postId,
            @RequestBody @Valid UpdatePostRequest updatePostRequest,
            HttpServletRequest httpRequest
    ) {
        String userId = (String) httpRequest.getAttribute("userId");
        UpdatePostCommand command = toUpdatePostCommand(postId, userId, updatePostRequest);
        postService.updatePost(command);

        return ResponseEntity.ok(new UpdatePostResponse(true));
    }

    @DeleteMapping("/{postId}")
    @RequiresAuth
    public ResponseEntity<DeletePostResponse> deletePost(
            @PathVariable UUID postId,
            HttpServletRequest httpRequest
    ) {
        var userId = (String) httpRequest.getAttribute("userId");
        DeletePostCommand command = new DeletePostCommand(userId, postId);
        postService.deletePost(command);

        return ResponseEntity.ok(new DeletePostResponse(true));
    }

    @GetMapping("/search")
    public ResponseEntity<RetrievePostsResponse> keywordSearch(
            @RequestParam @NotBlank String keyword,
            @RequestParam(defaultValue = "0") long page,
            @RequestParam(defaultValue = "10") long size
    ) {
        SearchPostByKeywordCommand command = new SearchPostByKeywordCommand(keyword, page, size);
        RetrievePostsResult result = postSearchService.searchByKeyword(command);

        return ResponseEntity.ok(toRetrievePostsResponse(result));
    }

    @GetMapping("/tags/{tag}")
    public ResponseEntity<RetrievePostsResponse> tagSearch(
            @PathVariable @NotBlank String tag,
            @RequestParam(defaultValue = "0") long page,
            @RequestParam(defaultValue = "10") long size
    ) {
        SearchPostByTagCommand command = new SearchPostByTagCommand(tag, page, size);
        RetrievePostsResult result = postSearchService.searchByTag(command);

        return ResponseEntity.ok(toRetrievePostsResponse(result));
    }
}
