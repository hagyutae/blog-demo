package com.sb02.blogdemo.core.image.port;

import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

public interface ImageFileStoragePort {
    ImageFileInfo saveImageFile(UUID imageId, MultipartFile multipartFile);
    Optional<ImageFileInfo> findImageFile(String filePath);
    void deleteImageFile(String filePath);
}
