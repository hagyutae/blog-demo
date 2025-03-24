package com.sb02.blogdemo.user.domain.usecase;

public interface LoginUserUseCase {
    LoginUserResult login(LoginUserCommand command);
}
