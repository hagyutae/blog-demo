package com.sb02.blogdemo.core.image.usecase.dto;

import org.springframework.web.multipart.MultipartFile;

public record SaveImageCommand(
        MultipartFile file
) {
}
