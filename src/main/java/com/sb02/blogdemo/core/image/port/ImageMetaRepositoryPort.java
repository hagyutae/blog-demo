package com.sb02.blogdemo.core.image.port;

import com.sb02.blogdemo.core.image.entity.ImageMeta;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ImageMetaRepositoryPort {
    void saveImageMeta(ImageMeta imageMeta);
    Optional<ImageMeta> findById(UUID imageId);
    void deleteImageMeta(UUID imageId);
    List<ImageMeta> findAll();
}
