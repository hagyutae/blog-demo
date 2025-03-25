package com.sb02.blogdemo.adapter.inbound.post;

import com.sb02.blogdemo.auth.RequiresAuth;
import com.sb02.blogdemo.core.posting.usecase.PostService;
import com.sb02.blogdemo.core.posting.usecase.PublishPostCommand;
import com.sb02.blogdemo.core.posting.usecase.PublishPostResult;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
