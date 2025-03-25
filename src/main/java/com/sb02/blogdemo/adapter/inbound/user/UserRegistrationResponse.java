package com.sb02.blogdemo.adapter.inbound.user;

public record UserRegistrationResponse(
        boolean success,
        String message
) {
}
