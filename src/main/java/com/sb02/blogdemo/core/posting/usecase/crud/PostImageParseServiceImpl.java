package com.sb02.blogdemo.core.posting.usecase.crud;

import com.sb02.blogdemo.core.image.usecase.FindImageUseCase;
import com.sb02.blogdemo.core.posting.entity.PostImage;
import com.sb02.blogdemo.core.posting.exception.InvalidPostImageError;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class PostImageParseServiceImpl implements PostImageParseService {

    private static final Pattern IMAGE_REFERENCE_PATTERN =  Pattern.compile("\\{\\{([^|]+)\\|([^}]+)\\}\\}");

    private final FindImageUseCase findImageUseCase;

    @Override
    public List<PostImage> parseImages(UUID postId, String content) {
        List<UUID> imageIds = new ArrayList<UUID>();

        Matcher matcher = IMAGE_REFERENCE_PATTERN.matcher(content);

        while (matcher.find()) {
            String imageIdStr = matcher.group(1).trim();
            try {
                UUID imageId = UUID.fromString(imageIdStr);
                imageIds.add(imageId);
            } catch (IllegalArgumentException e) {
                throw new InvalidPostImageError("Invalid image id: " + imageIdStr);
            }
        }

        return imageIds.stream().map(imageId -> {
            validateImageId(imageId);
            return PostImage.of(postId, imageId);
        }).toList();
    }

    private void validateImageId(UUID imageId) {
        boolean exists = findImageUseCase.existsById(imageId);
        if (!exists) {
            throw new InvalidPostImageError("Image not found: " + imageId);
        }
    }
}
