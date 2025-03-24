package com.sb02.blogdemo.user.in;

public record UserLoginResponse(
        boolean success,
        String token
) {
}
