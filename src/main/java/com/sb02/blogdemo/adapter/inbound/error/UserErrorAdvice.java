package com.sb02.blogdemo.adapter.inbound.error;

import com.sb02.blogdemo.core.user.exception.UserAlreadyExistsError;
import com.sb02.blogdemo.core.user.exception.UserError;
import com.sb02.blogdemo.core.user.exception.UserLoginFailedError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserErrorAdvice {

    private static final Logger logger = LoggerFactory.getLogger(UserErrorAdvice.class);

    @ExceptionHandler(UserAlreadyExistsError.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsError(UserAlreadyExistsError e) {
        logger.error("UserAlreadyExistsError handled: ", e);
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(UserLoginFailedError.class)
    public ResponseEntity<ErrorResponse> handleUserLoginFailedError(UserLoginFailedError e) {
        logger.error("UserLoginFailedError handled: ", e);
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(UserError.class)
    public ResponseEntity<ErrorResponse> handleUserError(UserError e) {
        logger.error("UserError handled: ", e);
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }
}
