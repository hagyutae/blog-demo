package com.sb02.blogdemo.adapter.inbound.user;

import com.sb02.blogdemo.core.user.usecase.LoginUserCommand;
import com.sb02.blogdemo.core.user.usecase.LoginUserResult;
import com.sb02.blogdemo.core.user.usecase.RegisterUserCommand;
import com.sb02.blogdemo.core.user.usecase.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponse> registerUser(@RequestBody @Valid UserRegistrationRequest requestBody) {
        RegisterUserCommand command = createRegisterUserCommand(requestBody);
        userService.registerUser(command);

        return ResponseEntity.ok(new UserRegistrationResponse(true, "User registered successfully"));
    }

    private RegisterUserCommand createRegisterUserCommand(UserRegistrationRequest requestBody) {
        return new RegisterUserCommand(
                requestBody.id(),
                requestBody.password(),
                requestBody.email(),
                requestBody.nickname()
        );
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> loginUser(@RequestBody @Valid UserLoginRequest requestBody) {
        LoginUserCommand command = createLoginUserCommand(requestBody);
        LoginUserResult result = userService.login(command);

        return ResponseEntity.ok(new UserLoginResponse(true, result.token()));
    }

    private LoginUserCommand createLoginUserCommand(UserLoginRequest requestBody) {
        return new LoginUserCommand(
                requestBody.id(),
                requestBody.password()
        );
    }
}
