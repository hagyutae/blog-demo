package com.sb02.blogdemo.adapter.inbound.image;

import com.sb02.blogdemo.adapter.inbound.dto.ErrorResponse;
import com.sb02.blogdemo.core.image.exception.ImageException;
import com.sb02.blogdemo.core.image.exception.ImageMetaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ImageControllerAdvice {
    private static final Logger logger = LoggerFactory.getLogger(ImageControllerAdvice.class);

    @ExceptionHandler(ImageMetaException.class)
    public ResponseEntity<ErrorResponse> handleImageMetaException(ImageMetaException e) {
        logger.error("ImageMetaException handled: ", e);
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(ImageException.class)
    public ResponseEntity<ErrorResponse> handleImageException(ImageException e) {
        logger.error("ImageException handled: ", e);
        return ResponseEntity.internalServerError().body(new ErrorResponse(e.getMessage()));
    }
}
