package com.sb02.blogdemo.adapter.outbound.image;

import com.sb02.blogdemo.adapter.outbound.SimpleFileRepository;
import com.sb02.blogdemo.core.image.entity.ImageMeta;
import com.sb02.blogdemo.core.image.port.ImageMetaRepositoryPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class ImageMetaRepository extends SimpleFileRepository<UUID, ImageMeta> implements ImageMetaRepositoryPort {

    public ImageMetaRepository(@Value("${file.entity}") String entityDir) {
        super(entityDir, "image_meta.ser");
    }

    @Override
    public void saveImageMeta(ImageMeta imageMeta) {
        data.put(imageMeta.getId(), imageMeta);
        saveData();
    }
}
