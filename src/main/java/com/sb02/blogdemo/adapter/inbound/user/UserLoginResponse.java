package com.sb02.blogdemo.adapter.inbound.user;

public record UserLoginResponse(
        boolean success,
        String token
) {
}
