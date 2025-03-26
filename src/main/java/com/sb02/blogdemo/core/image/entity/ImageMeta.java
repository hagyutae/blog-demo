package com.sb02.blogdemo.core.image.entity;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import static com.sb02.blogdemo.core.image.exception.ImageErrors.invalidImageMetaAttributeError;

@Getter
public class ImageMeta implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;
    private String originalName;
    private String extension;
    private String path;
    private long size;
    private Instant uploadedAt;

    private ImageMeta(
            String fileName,
            String path,
            long size,
            Instant uploadedAt
    ) {
        this.id = UUID.randomUUID();
        this.originalName = truncateName(extractOriginalName(fileName));
        this.extension = extractExtension(fileName);
        this.path = path;
        this.size = size;
        this.uploadedAt = uploadedAt;
    }

    public static ImageMeta create(String fileName, String path, long size) {
        Validator.validate(fileName);
        return new ImageMeta(fileName, path, size, Instant.now());
    }

    public void updatePath(String path) {
        this.path = path;
    }

    private String truncateName(String originalName) {
        return originalName.substring(0, Math.min(originalName.length(), 32));
    }

    private static String extractOriginalName(String fileName) {
        String[] nameSplit = fileName.split("\\.");
        return nameSplit[0];
    }

    private static String extractExtension(String fileName) {
        String[] nameSplit = fileName.split("\\.");
        if (nameSplit.length > 1) {
            return "." + nameSplit[1].toLowerCase();
        } else {
            return "";
        }
    }

    private static class Validator {

        public static void validate(String fileName) {
            validateFileName(fileName);
            validateExtension(fileName);
        }

        public static void validateFileName(String fileName) {
            String fileNameWithoutExtension = extractOriginalName(fileName);
            if (fileNameWithoutExtension.isBlank()) {
                throw invalidImageMetaAttributeError("filename", "null or blank value not allowed");
            }
        }

        public static void validateExtension(String fileName) {
            String extension = extractExtension(fileName);

            for (String allowedExtension : ImageExtension.getAllExtensions()) {
                if (extension.equals(allowedExtension)) {
                    return;
                }
            }
            throw invalidImageMetaAttributeError("extension", "not allowed: " + extension);
        }
    }


}
