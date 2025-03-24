package com.sb02.blogdemo.user.domain.exception;

public class UserLoginFailed extends UserException {
    public UserLoginFailed(String message) {
        super(message);
    }
}
