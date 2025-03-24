package com.sb02.blogdemo.user.domain.usecase;

public record LoginUserCommand(
        String id,
        String password
) {
}
