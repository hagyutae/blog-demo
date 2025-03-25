package com.sb02.blogdemo.core.image.port;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface ImageFileStoragePort {
    SaveFileResult saveImageFile(UUID imageId, MultipartFile multipartFile);
}
