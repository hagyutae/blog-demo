package com.sb02.blogdemo.user.domain.exception;

public class UserAlreadyExists extends UserException {
    public UserAlreadyExists(String message) {
        super(message);
    }
}
