package com.sb02.blogdemo.core.posting.usecase;

import java.util.List;

public record RetrievePostsResult(
        List<RetrievePostResult> posts,
        long currentPage,
        long pageSize,
        long totalPages
) {
}
