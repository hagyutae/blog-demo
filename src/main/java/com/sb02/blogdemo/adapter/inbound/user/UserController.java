package com.sb02.blogdemo.adapter.inbound.user;

import com.sb02.blogdemo.adapter.inbound.user.dto.UserLoginRequest;
import com.sb02.blogdemo.adapter.inbound.user.dto.UserLoginResponse;
import com.sb02.blogdemo.adapter.inbound.user.dto.UserRegistrationRequest;
import com.sb02.blogdemo.adapter.inbound.user.dto.UserRegistrationResponse;
import com.sb02.blogdemo.core.user.usecase.dto.LoginUserCommand;
import com.sb02.blogdemo.core.user.usecase.dto.LoginUserResult;
import com.sb02.blogdemo.core.user.usecase.dto.RegisterUserCommand;
import com.sb02.blogdemo.core.user.usecase.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.sb02.blogdemo.adapter.inbound.user.UserDtoMapper.toLoginUserCommand;
import static com.sb02.blogdemo.adapter.inbound.user.UserDtoMapper.toRegisterUserCommand;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponse> registerUser(@RequestBody @Valid UserRegistrationRequest requestBody) {
        RegisterUserCommand command = toRegisterUserCommand(requestBody);
        userService.registerUser(command);

        return ResponseEntity.ok(new UserRegistrationResponse(true, "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> loginUser(@RequestBody @Valid UserLoginRequest requestBody) {
        LoginUserCommand command = toLoginUserCommand(requestBody);
        LoginUserResult result = userService.login(command);

        return ResponseEntity.ok(new UserLoginResponse(true, result.token()));
    }

}
