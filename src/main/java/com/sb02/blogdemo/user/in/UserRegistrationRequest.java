package com.sb02.blogdemo.user.in;

public record UserRegistrationRequest(
        String id,
        String password,
        String email,
        String nickname
) {
}
