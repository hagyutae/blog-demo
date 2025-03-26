package com.sb02.blogdemo.core.user.usecase;

import com.sb02.blogdemo.core.user.usecase.dto.LoginUserCommand;
import com.sb02.blogdemo.core.user.usecase.dto.LoginUserResult;

public interface LoginUserUseCase {
    LoginUserResult login(LoginUserCommand command);
}
