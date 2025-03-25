package com.sb02.blogdemo.adapter.inbound.post;

import com.sb02.blogdemo.ErrorResponse;
import com.sb02.blogdemo.core.posting.exception.PostException;
import com.sb02.blogdemo.core.posting.exception.PostImageLostError;
import com.sb02.blogdemo.core.posting.exception.PostNotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PostControllerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(PostControllerAdvice.class);

    @ExceptionHandler(PostImageLostError.class)
    public ResponseEntity<ErrorResponse> handlePostImageLostError(PostImageLostError e) {
        logger.error("PostImageLostError handled: ", e);
        return ResponseEntity.internalServerError().body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(PostNotFound.class)
    public ResponseEntity<ErrorResponse> handlePostNotFound(PostNotFound e) {
        logger.error("PostNotFound handled: ", e);
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(PostException.class)
    public ResponseEntity<ErrorResponse> handlePostException(PostException e) {
        logger.error("PostException handled: ", e);
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }
}
