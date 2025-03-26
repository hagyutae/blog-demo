package com.sb02.blogdemo.adapter.inbound.post.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record PublishPostRequest(
        @NotBlank String title,
        @NotBlank String content,
        List<String> tags
) {
}
