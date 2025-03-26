package com.sb02.blogdemo.adapter.outbound.image;

import com.sb02.blogdemo.core.image.entity.ImageExtension;
import com.sb02.blogdemo.core.image.port.ImageFileInfo;
import com.sb02.blogdemo.core.image.port.ImageFileStoragePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Stream;

import static com.sb02.blogdemo.core.image.exception.ImageErrors.invalidImageFileError;
import static com.sb02.blogdemo.core.image.exception.ImageErrors.invalidImageUploadDirectoryError;

@Component
public class LocalImageFileStorage implements ImageFileStoragePort {

    private static final Logger logger = LoggerFactory.getLogger(LocalImageFileStorage.class);

    private final Path storagePath;
    private final Path uploadDirPath;

    public LocalImageFileStorage(
            @Value("${storage.path}") String storageDir
    ) {
        this.storagePath = Paths.get(storageDir);
        this.uploadDirPath = storagePath.resolve("upload");
        if (Files.notExists(uploadDirPath)) {
            try {
                Files.createDirectories(uploadDirPath);
            } catch (IOException e) {
                throw invalidImageUploadDirectoryError(e.getMessage());
            }
        }
    }

    @Override
    public ImageFileInfo saveImageFile(UUID imageId, MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw invalidImageFileError("empty file");
        }

        String storedFileName = imageId + "-" + multipartFile.getOriginalFilename();
        Path destination = uploadDirPath.resolve(storedFileName);
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Files.copy(inputStream, destination, StandardCopyOption.REPLACE_EXISTING);
            return createFilePath(storedFileName);
        } catch (IOException e) {
            throw invalidImageFileError(e.getMessage());
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

    @Override
    public void deleteImageFile(String filePath) {
        Path path = storagePath.resolve(filePath);
        if (Files.exists(path)) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                throw invalidImageFileError(e.getMessage());
            }
        }
    }

    @Override
    public Set<String> findAllImageFiles() {
        Set<String> imagePaths = new HashSet<>();

        // Use the stream within a try-with-resources block to ensure proper closing
        try (Stream<Path> pathStream = Files.walk(uploadDirPath, Integer.MAX_VALUE)) {
            pathStream
                    .filter(Files::isRegularFile)
                    .filter(this::isImageFile)
                    .map(Path::toString)
                    .forEach(imagePaths::add);

            logger.info("Found {} image files in upload directory", imagePaths.size());
        } catch (IOException e) {
            logger.error("Error accessing files in upload directory: {}", e.getMessage());
        }

        return imagePaths;
    }

    private boolean isImageFile(Path path) {
        String fileName = path.getFileName().toString().toLowerCase();
        for (String extension : ImageExtension.getAllExtensions()) {
            if (fileName.endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String createFullFilePath(String filePath) {
        return storagePath.resolve(filePath).toString();
    }
}
