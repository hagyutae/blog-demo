package com.sb02.blogdemo.core.user.usecase.dto;

public record RegisterUserCommand(
    String id,
    String password,
    String email,
    String nickname
) {
}
