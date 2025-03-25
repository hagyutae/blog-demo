package com.sb02.blogdemo.user.core.usecase;

public record RegisterUserCommand(
    String id,
    String password,
    String email,
    String nickname
) {
}
