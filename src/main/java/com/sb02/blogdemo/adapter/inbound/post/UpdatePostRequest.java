package com.sb02.blogdemo.adapter.inbound.post;

import java.util.List;

public record UpdatePostRequest(
        String title,
        String content,
        List<String> tags
) {
}
