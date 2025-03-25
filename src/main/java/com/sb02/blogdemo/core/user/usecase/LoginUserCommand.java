package com.sb02.blogdemo.core.user.usecase;

public record LoginUserCommand(
        String id,
        String password
) {
}
