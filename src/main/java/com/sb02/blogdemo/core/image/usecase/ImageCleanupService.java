package com.sb02.blogdemo.core.image.usecase;

import com.sb02.blogdemo.core.image.entity.ImageMeta;
import com.sb02.blogdemo.core.image.port.ImageFileStoragePort;
import com.sb02.blogdemo.core.image.port.ImageMetaRepositoryPort;
import com.sb02.blogdemo.core.post.entity.Post;
import com.sb02.blogdemo.core.post.entity.PostImage;
import com.sb02.blogdemo.core.post.port.PostImageRepositoryPort;
import com.sb02.blogdemo.core.post.port.PostRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageCleanupService {

    private static final Logger logger = LoggerFactory.getLogger(ImageCleanupService.class);
    private static final Pattern IMAGE_REFERENCE_PATTERN = Pattern.compile("\\{\\{([^|]+)\\|([^}]+)\\}\\}");

    private static final long UNUSED_IMAGE_THRESHOLD_HOURS = 24;

    private final ImageMetaRepositoryPort imageMetaRepository;
    private final ImageFileStoragePort imageFileStorage;
    private final PostRepositoryPort postRepository;
    private final PostImageRepositoryPort postImageRepository;

    public int cleanupUnusedPostImages() {
        logger.info("Starting image cleanup process");

        Set<UUID> referencedImageIds = findReferencedImageIds();
        logger.info("Found {} image IDs referenced in posts", referencedImageIds.size());

        Set<UUID> postImageIds = findAllPostImageIds();
        logger.info("Found {} image IDS referenced as post-images", postImageIds.size());

        Set<UUID> orphanedImageIds = new HashSet<>(postImageIds);
        orphanedImageIds.removeAll(referencedImageIds);
        logger.info("Found {} orphaned images to delete", orphanedImageIds.size());

        int deletedCount = deleteOrphanedImages(orphanedImageIds);
        logger.info("Successfully deleted {} orphaned images", deletedCount);

        return deletedCount;
    }

    private Set<UUID> findReferencedImageIds() {
        List<Post> allPosts = postRepository.findAll();

        return allPosts.stream()
                .map(Post::getContent)
                .flatMap(content -> {
                    Matcher matcher = IMAGE_REFERENCE_PATTERN.matcher(content);
                    Set<UUID> imageIds = new HashSet<>();

                    while (matcher.find()) {
                        String imageIdStr = matcher.group(1).trim();
                        try {
                            UUID imageId = UUID.fromString(imageIdStr);
                            imageIds.add(imageId);
                        } catch (IllegalArgumentException e) {
                            logger.warn("Invalid image ID format in post content: {}", imageIdStr);
                        }
                    }

                    return imageIds.stream();
                })
                .collect(Collectors.toSet());
    }

    private Set<UUID> findAllPostImageIds() {
        List<Post> allPosts = postRepository.findAll();

        return allPosts.stream()
                .map(Post::getId)
                .flatMap(postId -> postImageRepository.retrieveImages(postId).stream())
                .map(PostImage::getImageId)
                .collect(Collectors.toSet());
    }

    private int deleteOrphanedImages(Set<UUID> imageIds) {
        int deletedCount = 0;

        for (UUID imageId : imageIds) {
            try {
                Optional<ImageMeta> imageMeta = imageMetaRepository.findById(imageId);

                if (imageMeta.isPresent()) {
                    imageFileStorage.deleteImageFile(imageMeta.get().getPath());
                    imageMetaRepository.deleteImageMeta(imageId);

                    logger.info("Deleted orphaned image: {}", imageId);

                    deletedCount++;
                }
            } catch (Exception e) {
                logger.error("Error deleting orphaned image {}: {}", imageId, e.getMessage());
            }
        }

        return deletedCount;
    }

    public int cleanupOrphanedImageFiles() {
        Set<String> allReferencedImageFiles = findAllReferencedImageFiles();
        logger.info("Found {} referenced image files", allReferencedImageFiles.size());

        Set<String> allImageMetaReferencedImageFiles = findAllMetaReferencedImageFiles();

        Set<String> allImageFiles = imageFileStorage.findAllImageFiles();
        logger.info("Found {} image files in storage", allImageFiles.size());

        Set<String> orphanedFiles = new HashSet<>(allImageFiles);
        orphanedFiles.removeAll(allReferencedImageFiles);
        orphanedFiles.removeAll(allImageMetaReferencedImageFiles);

        int deletedCount = deleteOrphanedFiles(orphanedFiles);
        logger.info("Successfully deleted {} orphaned image files", deletedCount);

        return deletedCount;
    }

    private Set<String> findAllReferencedImageFiles() {
        return findAllPostImageIds().stream()
                .flatMap(id -> imageMetaRepository.findById(id).stream())
                .map(ImageMeta::getPath)
                .map(imageFileStorage::createFullFilePath)
                .collect(Collectors.toSet());
    }

    private Set<String> findAllMetaReferencedImageFiles() {
        return imageMetaRepository.findAll().stream()
                .map(ImageMeta::getPath)
                .map(imageFileStorage::createFullFilePath)
                .collect(Collectors.toSet());
    }

    private int deleteOrphanedFiles(Set<String> orphanedFiles) {
        int deletedCount = 0;

        for (String filePath : orphanedFiles) {
            try {
                imageFileStorage.deleteImageFile(filePath);
                deletedCount++;
            } catch (Exception e) {
                logger.error("Error deleting orphaned image file {}: {}", filePath, e.getMessage());
            }
        }

        return deletedCount;
    }
}