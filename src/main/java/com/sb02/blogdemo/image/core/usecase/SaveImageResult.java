package com.sb02.blogdemo.image.core.usecase;

import java.util.UUID;

public record SaveImageResult(
        UUID imageId,
        String uploadPath
) {
}
