package com.sb02.blogdemo.user.core.usecase;

public record LoginUserCommand(
        String id,
        String password
) {
}
