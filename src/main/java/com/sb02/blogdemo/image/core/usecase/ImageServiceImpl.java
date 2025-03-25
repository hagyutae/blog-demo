package com.sb02.blogdemo.image.core.usecase;

import com.sb02.blogdemo.image.core.entity.ImageMeta;
import com.sb02.blogdemo.image.core.port.ImageFileStoragePort;
import com.sb02.blogdemo.image.core.port.ImageMetaRepositoryPort;
import com.sb02.blogdemo.image.core.port.SaveFileResult;
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
