package com.sb02.blogdemo.adapter.inbound.error;

import com.sb02.blogdemo.core.posting.exception.InvalidPostAccessError;
import com.sb02.blogdemo.core.posting.exception.PostError;
import com.sb02.blogdemo.core.posting.exception.PostNotFoundError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class PostErrorAdvice {

    private static final Logger logger = LoggerFactory.getLogger(PostErrorAdvice.class);

    @ExceptionHandler(PostNotFoundError.class)
    public ResponseEntity<ErrorResponse> handlePostNotFoundError(PostNotFoundError e) {
        logger.error("PostNotFoundError handled: ", e);
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(InvalidPostAccessError.class)
    public ResponseEntity<ErrorResponse> handleInvalidPostAccessError(InvalidPostAccessError e) {
        logger.error("InvalidPostAccessError handled: ", e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(PostError.class)
    public ResponseEntity<ErrorResponse> handlePostError(PostError e) {
        logger.error("PostError handled: ", e);
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }
}
