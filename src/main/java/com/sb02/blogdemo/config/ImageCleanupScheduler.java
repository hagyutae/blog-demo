package com.sb02.blogdemo.config;

import com.sb02.blogdemo.core.image.usecase.ImageCleanupService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class ImageCleanupScheduler {

    private static final Logger logger = LoggerFactory.getLogger(ImageCleanupScheduler.class);

    private final ImageCleanupService imageCleanupService;

    @Scheduled(fixedRate = 60000) // 60,000 ms = 1 minute
    public void scheduleImageCleanup() {
        logger.info("Starting scheduled image cleanup task");

        try {
            int cleanedCount = imageCleanupService.cleanupUnusedPostImages();
            logger.info("Scheduled image cleanup completed: {} images removed", cleanedCount);

            int cleanedFileCount = imageCleanupService.cleanupOrphanedImageFiles();
            logger.info("Scheduled image cleanup completed: {} files removed", cleanedFileCount);
        } catch (Exception e) {
            logger.error("Error during scheduled image cleanup: {}", e.getMessage(), e);
        }
    }
}