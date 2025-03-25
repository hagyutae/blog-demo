package com.sb02.blogdemo.image.core.entity;

import com.sb02.blogdemo.image.core.exception.ImageMetaException;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class ImageMeta implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;
    private String originalName;
    private String extension;
    private String path;
    private Long size;
    private Instant uploadedAt;

    private ImageMeta(
            String fileName,
            String path,
            Long size,
            Instant uploadedAt
    ) {
        validateExtension(fileName);

        this.id = UUID.randomUUID();
        this.originalName = truncateName(extractOriginalName(fileName));
        this.extension = extractExtension(fileName);
        this.path = path;
        this.size = size;
        this.uploadedAt = uploadedAt;
    }

    public static ImageMeta create(String fileName, String path, Long size) {
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

    public static void validateExtension(String fileName) {
        // 이미지 파일 확장자 검증(jpg, jpeg, png, gif만 허용)
        String[] allowedExtensions = {".jpg", ".jpeg", ".png", ".gif"};

        String extension = extractExtension(fileName);

        for (String allowedExtension : allowedExtensions) {
            if (extension.equals(allowedExtension)) {
                return;
            }
        }
        throw new ImageMetaException("extension not allowed: " + extension);
    }
}
