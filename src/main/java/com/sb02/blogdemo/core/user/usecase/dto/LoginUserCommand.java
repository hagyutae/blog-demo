package com.sb02.blogdemo.core.user.usecase.dto;

public record LoginUserCommand(
        String id,
        String password
) {
}
