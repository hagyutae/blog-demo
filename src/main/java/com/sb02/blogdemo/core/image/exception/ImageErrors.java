package com.sb02.blogdemo.core.image.exception;

import java.util.UUID;

public final class ImageErrors {

    private ImageErrors() {}

    public static final String INVALID_IMAGE_FILE_MESSAGE = "Image file error: %s";
    public static final String INVALID_IMAGE_META_ATTRIBUTE_MESSAGE = "Invalid image meta attribute %s: %s";
    public static final String INVALID_IMAGE_UPLOAD_DIRECTORY_MESSAGE = "Invalid image upload directory: %s";
    public static final String IMAGE_NOT_FOUND_MESSAGE = "Image %s not found";

    public static InvalidImageFileError invalidImageFileError(String message) {
        return new InvalidImageFileError(String.format(INVALID_IMAGE_FILE_MESSAGE, message));
    }

    public static InvalidImageMetaAttributeError invalidImageMetaAttributeError(String attribute, String details) {
        return new InvalidImageMetaAttributeError(String.format(INVALID_IMAGE_META_ATTRIBUTE_MESSAGE, attribute, details));
    }

    public static InvalidImageUploadDirectoryError invalidImageUploadDirectoryError(String message) {
        return new InvalidImageUploadDirectoryError(String.format(INVALID_IMAGE_UPLOAD_DIRECTORY_MESSAGE, message));
    }

    public static ImageNotFoundError imageNotFoundError(UUID imageId) {
        return new ImageNotFoundError(String.format(IMAGE_NOT_FOUND_MESSAGE, imageId));
    }
}
