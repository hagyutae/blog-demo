package com.sb02.blogdemo.user.domain.usecase;

public record RegisterUserCommand(
    String id,
    String password,
    String email,
    String nickname
) {
}
