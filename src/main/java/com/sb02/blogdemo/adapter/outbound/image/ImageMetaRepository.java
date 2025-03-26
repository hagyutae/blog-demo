package com.sb02.blogdemo.adapter.outbound.image;

import com.sb02.blogdemo.adapter.outbound.SimpleFileRepository;
import com.sb02.blogdemo.core.image.entity.ImageMeta;
import com.sb02.blogdemo.core.image.port.ImageMetaRepositoryPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ImageMetaRepository extends SimpleFileRepository<UUID, ImageMeta> implements ImageMetaRepositoryPort {

    public ImageMetaRepository(@Value("${storage.path}") String storageDir) {
        super(storageDir, "image_meta.ser");
    }

    @Override
    public void saveImageMeta(ImageMeta imageMeta) {
        data.put(imageMeta.getId(), imageMeta);
        saveData();
    }

    @Override
    public Optional<ImageMeta> findById(UUID imageId) {
        return Optional.ofNullable(data.get(imageId));
    }

    @Override
    public void deleteImageMeta(UUID imageId) {
        data.remove(imageId);
        saveData();
    }

    @Override
    public List<ImageMeta> findAll() {
        return new ArrayList<>(data.values());
    }
}
