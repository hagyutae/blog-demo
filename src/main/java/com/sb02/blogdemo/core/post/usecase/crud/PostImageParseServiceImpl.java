package com.sb02.blogdemo.core.post.usecase.crud;

import com.sb02.blogdemo.core.image.usecase.FindImageUseCase;
import com.sb02.blogdemo.core.post.entity.PostImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.sb02.blogdemo.core.image.exception.ImageErrors.imageNotFoundError;
import static com.sb02.blogdemo.core.post.exception.PostErrors.invalidPostImageIdError;

@Service
@RequiredArgsConstructor
public class PostImageParseServiceImpl implements PostImageParseService {

    private static final Pattern IMAGE_REFERENCE_PATTERN = Pattern.compile("\\{\\{([^|]+)\\|([^}]+)\\}\\}");

    private final FindImageUseCase findImageUseCase;

    @Override
    public List<PostImage> parseImages(UUID postId, String content) {
        return extractImageIdStrings(content)
                .map(this::parseImageId)
                .peek(this::validateImageId)
                .map(imageId -> PostImage.of(postId, imageId))
                .collect(Collectors.toList());
    }

    private Stream<String> extractImageIdStrings(String content) {
        return IMAGE_REFERENCE_PATTERN.matcher(content)
                .results()
                .map(matchResult -> matchResult.group(1).trim());
    }

    private UUID parseImageId(String imageIdStr) {
        try {
            return UUID.fromString(imageIdStr);
        } catch (IllegalArgumentException e) {
            throw invalidPostImageIdError(imageIdStr);
        }
    }

    private void validateImageId(UUID imageId) {
        if (!findImageUseCase.existsById(imageId)) {
            throw imageNotFoundError(imageId);
        }
    }
}