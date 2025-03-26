package com.sb02.blogdemo.adapter.inbound.user;

import com.sb02.blogdemo.adapter.inbound.dto.ErrorResponse;
import com.sb02.blogdemo.core.user.exception.UserAlreadyExistsError;
import com.sb02.blogdemo.core.user.exception.UserError;
import com.sb02.blogdemo.core.user.exception.UserLoginFailedError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserControllerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(UserControllerAdvice.class);

    @ExceptionHandler(UserAlreadyExistsError.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExistsError e) {
        logger.error("UserAlreadyExists handled: ", e);
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(UserLoginFailedError.class)
    public ResponseEntity<ErrorResponse> handleUserLoginFailed(UserLoginFailedError e) {
        logger.error("UserLoginFailed handled: ", e);
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(UserError.class)
    public ResponseEntity<ErrorResponse> handleUserException(UserError e) {
        logger.error("UserException handled: ", e);
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }
}
