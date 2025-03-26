package com.sb02.blogdemo.core.user.usecase;

import com.sb02.blogdemo.auth.JwtUtil;
import com.sb02.blogdemo.core.user.PasswordUtil;
import com.sb02.blogdemo.core.user.entity.User;
import com.sb02.blogdemo.core.user.port.UserRepositoryPort;
import com.sb02.blogdemo.core.user.usecase.dto.LoginUserCommand;
import com.sb02.blogdemo.core.user.usecase.dto.LoginUserResult;
import com.sb02.blogdemo.core.user.usecase.dto.RegisterUserCommand;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.sb02.blogdemo.core.user.exception.UserErrors.userAlreadyExistsError;
import static com.sb02.blogdemo.core.user.exception.UserErrors.userLoginFailedError;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    public static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepositoryPort userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public void registerUser(RegisterUserCommand command) {
        if (existsById(command.id())) {
            throw userAlreadyExistsError(command.id());
        }

        User user = User.create(command.id(), command.password(), command.email(), command.nickname());
        userRepository.save(user);

        logger.info("User registered: {}", user.getId());
    }

    @Override
    public LoginUserResult login(LoginUserCommand command) {
        Optional<User> found = userRepository.findById(command.id());
        if (found.isEmpty()) {
            throw userLoginFailedError(command.id(), "not found");
        }

        User user = found.get();

        boolean matched = PasswordUtil.checkPassword(command.password(), user.getPassword());

        if (!matched) {
            throw userLoginFailedError(command.id(), "password mismatch");
        }

        String token = jwtUtil.generateToken(user.getId());

        logger.info("User logged in: {}", user.getId());

        return new LoginUserResult(token);
    }

    @Override
    public boolean existsById(String userId) {
        Optional<User> found = userRepository.findById(userId);
        return found.isPresent();
    }
}
