package com.sb02.blogdemo.core.posting.exception;

import java.util.UUID;

public final class PostErrors {

    private PostErrors() {}

    public static final String INVALID_POST_ACCESS_MESSAGE = "User %s does not have access to this post";
    public static final String INVALID_POST_ATTRIBUTE_MESSAGE = "Post attribute %s is invalid: %s";
    public static final String INVALID_POST_IMAGE_ID_MESSAGE = "Invalid post image id %s";
    public static final String POST_NOT_FOUND_MESSAGE = "Post %s not found";

    public static InvalidPostAccessError invalidPostAccessError(String userId) {
        return new InvalidPostAccessError(String.format(INVALID_POST_ACCESS_MESSAGE, userId));
    }

    public static InvalidPostAttributeError invalidPostAttributeError(String attribute, String details) {
        return new InvalidPostAttributeError(String.format(INVALID_POST_ATTRIBUTE_MESSAGE, attribute, details));
    }

    public static InvalidPostImageIdError invalidPostImageIdError(String details) {
        return new InvalidPostImageIdError(String.format(INVALID_POST_IMAGE_ID_MESSAGE, details));
    }

    public static PostNotFoundError postNotFoundError(UUID postId) {
        return new PostNotFoundError(String.format(POST_NOT_FOUND_MESSAGE, postId));
    }
}
