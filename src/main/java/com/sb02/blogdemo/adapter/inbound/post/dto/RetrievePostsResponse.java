package com.sb02.blogdemo.adapter.inbound.post.dto;

import java.util.List;

public record RetrievePostsResponse(
        List<RetrievedPost> posts,
        long totalPages,
        long currentPage
) {
}
