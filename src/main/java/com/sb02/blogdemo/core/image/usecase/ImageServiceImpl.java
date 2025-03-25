package com.sb02.blogdemo.core.image.usecase;

import com.sb02.blogdemo.core.image.entity.ImageMeta;
import com.sb02.blogdemo.core.image.port.ImageFileStoragePort;
import com.sb02.blogdemo.core.image.port.ImageMetaRepositoryPort;
import com.sb02.blogdemo.core.image.port.SaveFileResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageFileStoragePort imageFileStorage;
    private final ImageMetaRepositoryPort imageMetaRepository;

    @Override
    public SaveImageResult saveImage(SaveImageCommand command) {
        MultipartFile fileObj = command.file();
        ImageMeta imageMeta = ImageMeta.create(fileObj.getOriginalFilename(), null, fileObj.getSize());
        SaveFileResult saveImageResult = imageFileStorage.saveImageFile(imageMeta.getId(), fileObj);
        imageMeta.updatePath(saveImageResult.filePath());
        imageMetaRepository.saveImageMeta(imageMeta);

        return new SaveImageResult(imageMeta.getId(), imageMeta.getPath());
    }
}
