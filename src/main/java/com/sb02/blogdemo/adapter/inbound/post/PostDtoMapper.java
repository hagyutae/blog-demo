package com.sb02.blogdemo.adapter.inbound.post;

import com.sb02.blogdemo.adapter.inbound.post.dto.*;
import com.sb02.blogdemo.core.posting.usecase.crud.dto.PublishPostCommand;
import com.sb02.blogdemo.core.posting.usecase.crud.dto.PublishPostResult;
import com.sb02.blogdemo.core.posting.usecase.crud.dto.UpdatePostCommand;
import com.sb02.blogdemo.core.posting.usecase.dto.RetrievePostResult;
import com.sb02.blogdemo.core.posting.usecase.dto.RetrievePostsResult;
import com.sb02.blogdemo.utils.TimeUtils;

import java.util.UUID;

public class PostDtoMapper {
    static PublishPostCommand toPublishPostCommand(PublishPostRequest publishPostRequest, String userId) {
        return new PublishPostCommand(
                publishPostRequest.title(),
                publishPostRequest.content(),
                userId,
                publishPostRequest.tags()
        );
    }

    static PublishPostResponse toPublishPostResponse(PublishPostResult publishPostResult) {
        return new PublishPostResponse(true, publishPostResult.postId().toString());
    }


    static RetrievedPost toRetrievedPost(RetrievePostResult retrievePostResult) {
        return new RetrievedPost(
                retrievePostResult.postId().toString(),
                retrievePostResult.title(),
                retrievePostResult.content(),
                retrievePostResult.authorId(),
                retrievePostResult.authorNickname(),
                TimeUtils.formatedTimeString(retrievePostResult.createdAt()),
                TimeUtils.formatedTimeString(retrievePostResult.updatedAt()),
                retrievePostResult.tags()
        );
    }

    static RetrievePostsResponse toRetrievePostsResponse(RetrievePostsResult retrievePostsResult) {
        return new RetrievePostsResponse(
                retrievePostsResult.posts().stream().map(PostDtoMapper::toRetrievedPost).toList(),
                retrievePostsResult.totalPages(),
                retrievePostsResult.currentPage()
        );
    }

    static UpdatePostCommand toUpdatePostCommand(UUID postId, String userId, UpdatePostRequest updatePostRequest) {
        return new UpdatePostCommand(
                userId,
                postId,
                updatePostRequest.title(),
                updatePostRequest.content(),
                updatePostRequest.tags()
        );
    }

}
