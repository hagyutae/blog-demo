package com.sb02.blogdemo.image.core.usecase;

import org.springframework.web.multipart.MultipartFile;

public record SaveImageCommand(
        MultipartFile file
) {
}
