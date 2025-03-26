package com.sb02.blogdemo.core.image.usecase;

import com.sb02.blogdemo.core.image.entity.ImageMeta;
import com.sb02.blogdemo.core.image.port.ImageFileInfo;
import com.sb02.blogdemo.core.image.port.ImageFileStoragePort;
import com.sb02.blogdemo.core.image.port.ImageMetaRepositoryPort;
import com.sb02.blogdemo.core.image.usecase.dto.SaveImageCommand;
import com.sb02.blogdemo.core.image.usecase.dto.SaveImageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.sb02.blogdemo.core.image.exception.ImageErrors.imageNotFoundError;
import static com.sb02.blogdemo.core.image.exception.ImageErrors.invalidImageFileError;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageFileStoragePort imageFileStorage;
    private final ImageMetaRepositoryPort imageMetaRepository;


    @Override
    public SaveImageResult saveImage(SaveImageCommand command) {
        MultipartFile fileObj = Objects.requireNonNull(command.file(), "File cannot be null");

        ImageMeta imageMeta = ImageMeta.create(
                Optional.ofNullable(fileObj.getOriginalFilename())
                        .orElseThrow(() -> invalidImageFileError("original filename cannot be null")),
                null,
                fileObj.getSize()
        );

        ImageFileInfo fileInfo = imageFileStorage.saveImageFile(imageMeta.getId(), fileObj);
        imageMeta.updatePath(fileInfo.filePath());
        imageMetaRepository.saveImageMeta(imageMeta);

        return new SaveImageResult(imageMeta.getId(), imageMeta.getPath());
    }

    @Override
    public boolean existsById(UUID imageId) {
        return imageMetaRepository.findById(imageId)
                .map(imageMeta -> {
                    boolean fileExists = imageFileStorage.findImageFile(imageMeta.getPath()).isPresent();
                    if (!fileExists) {
                        throw imageNotFoundError(imageId);
                    }
                    return true;
                })
                .orElse(false);
    }

    @Override
    public void deleteImage(UUID imageId) {
        imageMetaRepository.findById(imageId)
                .ifPresent(imageMeta -> {
                    imageFileStorage.deleteImageFile(imageMeta.getPath());
                    imageMetaRepository.deleteImageMeta(imageMeta.getId());
                });
    }
}
