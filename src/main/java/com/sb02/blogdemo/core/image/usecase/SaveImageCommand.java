package com.sb02.blogdemo.core.image.usecase;

import org.springframework.web.multipart.MultipartFile;

public record SaveImageCommand(
        MultipartFile file
) {
}
