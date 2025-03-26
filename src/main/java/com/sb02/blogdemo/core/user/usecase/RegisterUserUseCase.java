package com.sb02.blogdemo.core.user.usecase;

import com.sb02.blogdemo.core.user.usecase.dto.RegisterUserCommand;

public interface RegisterUserUseCase {
    void registerUser(RegisterUserCommand command);
}
