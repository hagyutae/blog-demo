package com.sb02.blogdemo.user.in;

public record UserLoginRequest(
        String id,
        String password
) {
}
