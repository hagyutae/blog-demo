package com.sb02.blogdemo.user.in;

public record UserRegistrationResponse(
        boolean success,
        String message
) {
}
