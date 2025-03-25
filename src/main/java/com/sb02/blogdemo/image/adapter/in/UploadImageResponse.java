package com.sb02.blogdemo.image.adapter.in;

public record UploadImageResponse(
        boolean success,
        String imageId,
        String path
) {
}
