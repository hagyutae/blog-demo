package com.sb02.blogdemo.image.core.port;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface ImageFileStoragePort {
    SaveFileResult saveImageFile(UUID imageId, MultipartFile multipartFile);
}
