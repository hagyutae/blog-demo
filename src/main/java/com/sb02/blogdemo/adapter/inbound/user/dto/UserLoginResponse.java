package com.sb02.blogdemo.adapter.inbound.user.dto;

public record UserLoginResponse(
        boolean success,
        String token
) {
}
