package com.sb02.blogdemo.user.adapter.in;

public record UserLoginResponse(
        boolean success,
        String token
) {
}
