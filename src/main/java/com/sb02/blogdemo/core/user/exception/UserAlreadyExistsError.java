package com.sb02.blogdemo.core.user.exception;

public class UserAlreadyExistsError extends UserError {
    public UserAlreadyExistsError(String message) {
        super(message);
    }
}
