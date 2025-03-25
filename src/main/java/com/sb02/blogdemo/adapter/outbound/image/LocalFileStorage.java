package com.sb02.blogdemo.adapter.outbound.image;

import com.sb02.blogdemo.core.image.port.ImageFileInfo;
import com.sb02.blogdemo.core.image.exception.ImageUploadDirectoryError;
import com.sb02.blogdemo.core.image.exception.ImageFileError;
import com.sb02.blogdemo.core.image.port.ImageFileStoragePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@Component
public class LocalFileStorage implements ImageFileStoragePort {

    private final Path storagePath;
    private final Path uploadDirPath;

    public LocalFileStorage(
            @Value("${storage.path}") String storageDir
    ) {
        this.storagePath = Paths.get(storageDir);
        this.uploadDirPath = storagePath.resolve("upload");
        if (Files.notExists(uploadDirPath)) {
            try {
                Files.createDirectories(uploadDirPath);
            } catch (IOException e) {
                throw new ImageUploadDirectoryError(e.getMessage());
            }
        }
    }

    @Override
    public ImageFileInfo saveImageFile(UUID imageId, MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new ImageFileError("Empty file");
        }

        String storedFileName = imageId + "-" + multipartFile.getOriginalFilename();
        Path destination = uploadDirPath.resolve(storedFileName);
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
            return createFilePath(storedFileName);
        } catch (IOException e) {
            throw new ImageFileError(e.getMessage());
        }
    }

    private ImageFileInfo createFilePath(String storedFileName) {
        String storedFilePath = "upload/" + storedFileName;
        return new ImageFileInfo(storedFileName, storedFilePath);
    }

    @Override
    public Optional<ImageFileInfo> findImageFile(String filePath) {
        Path path = storagePath.resolve(filePath);
        if (Files.notExists(path)) {
            return Optional.empty();
        }
        return Optional.of(new ImageFileInfo(path.getFileName().toString(), filePath));
    }
}
