package com.sb02.blogdemo.core.image.usecase;

import java.util.UUID;

public record SaveImageResult(
        UUID imageId,
        String uploadPath
) {
}
