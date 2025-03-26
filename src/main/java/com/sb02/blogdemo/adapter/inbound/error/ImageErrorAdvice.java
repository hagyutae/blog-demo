package com.sb02.blogdemo.adapter.inbound.error;

import com.sb02.blogdemo.core.image.exception.ImageError;
import com.sb02.blogdemo.core.image.exception.ImageNotFoundError;
import com.sb02.blogdemo.core.image.exception.InvalidImageMetaAttributeError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ImageErrorAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ImageErrorAdvice.class);

    @ExceptionHandler(InvalidImageMetaAttributeError.class)
    public ResponseEntity<ErrorResponse> handleInvalidImageMetaAttributeError(InvalidImageMetaAttributeError e) {
        logger.error("InvalidImageMetaAttributeError handled: ", e);
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(ImageNotFoundError.class)
    public ResponseEntity<ErrorResponse> handleImageNotFoundError(ImageNotFoundError e) {
        logger.error("ImageNotFoundError handled: ", e);
        return ResponseEntity.internalServerError().body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(ImageError.class)
    public ResponseEntity<ErrorResponse> handleImageError(ImageError e) {
        logger.error("ImageError handled: ", e);
        return ResponseEntity.internalServerError().body(new ErrorResponse(e.getMessage()));
    }
}
