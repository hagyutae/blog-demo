package com.sb02.blogdemo.adapter.inbound.user;

import com.sb02.blogdemo.adapter.inbound.user.dto.UserLoginRequest;
import com.sb02.blogdemo.adapter.inbound.user.dto.UserRegistrationRequest;
import com.sb02.blogdemo.core.user.usecase.dto.LoginUserCommand;
import com.sb02.blogdemo.core.user.usecase.dto.RegisterUserCommand;

public class UserDtoMapper {
    static RegisterUserCommand toRegisterUserCommand(UserRegistrationRequest requestBody) {
        return new RegisterUserCommand(
                requestBody.id(),
                requestBody.password(),
                requestBody.email(),
                requestBody.nickname()
        );
    }

    static LoginUserCommand toLoginUserCommand(UserLoginRequest requestBody) {
        return new LoginUserCommand(
                requestBody.id(),
                requestBody.password()
        );
    }
}
