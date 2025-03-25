package com.sb02.blogdemo.adapter.inbound.image;

public record UploadImageResponse(
        boolean success,
        String imageId,
        String path
) {
}
