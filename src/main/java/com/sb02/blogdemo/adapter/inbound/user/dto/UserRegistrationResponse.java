package com.sb02.blogdemo.adapter.inbound.user.dto;

public record UserRegistrationResponse(
        boolean success,
        String message
) {
}
