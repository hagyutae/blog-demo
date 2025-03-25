package com.sb02.blogdemo.image.adapter.out.storage;

import com.sb02.blogdemo.image.core.port.SaveFileResult;
import com.sb02.blogdemo.image.core.exception.ImageUploadDirectoryError;
import com.sb02.blogdemo.image.core.exception.ImageFileError;
import com.sb02.blogdemo.image.core.port.ImageFileStoragePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
public class LocalFileStorage implements ImageFileStoragePort {

    private final Path uploadDirPath;
    private final String uploadUrl;

    public LocalFileStorage(
            @Value("${file.upload.dir}") String uploadDir,
            @Value("${file.upload.url}") String uploadUrl
    ) {
        this.uploadDirPath = Paths.get(uploadDir);
        if (Files.notExists(uploadDirPath)) {
            try {
                Files.createDirectories(uploadDirPath);
            } catch (IOException e) {
                throw new ImageUploadDirectoryError(e.getMessage());
            }
        }
        this.uploadUrl = uploadUrl;
    }

    @Override
    public SaveFileResult saveImageFile(UUID imageId, MultipartFile multipartFile) {
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

    private SaveFileResult createFilePath(String storedFileName) {
        String storedFilePath = uploadUrl + "/" + storedFileName;
        return new SaveFileResult(storedFileName, storedFilePath);
    }
}
