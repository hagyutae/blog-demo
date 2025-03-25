package com.sb02.blogdemo.adapter.inbound.user;

import com.sb02.blogdemo.ErrorResponse;
import com.sb02.blogdemo.core.user.exception.UserAlreadyExists;
import com.sb02.blogdemo.core.user.exception.UserException;
import com.sb02.blogdemo.core.user.exception.UserLoginFailed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserControllerAdvice {

    private static final Logger logger = LoggerFactory.getLogger(UserControllerAdvice.class);

    @ExceptionHandler(UserAlreadyExists.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExists e) {
        logger.error("UserAlreadyExists handled: ", e);
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(UserLoginFailed.class)
    public ResponseEntity<ErrorResponse> handleUserLoginFailed(UserLoginFailed e) {
        logger.error("UserLoginFailed handled: ", e);
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorResponse> handleUserException(UserException e) {
        logger.error("UserException handled: ", e);
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }
}
