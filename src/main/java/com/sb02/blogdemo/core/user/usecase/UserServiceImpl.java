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

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepositoryPort userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public void registerUser(RegisterUserCommand command) {
        String userId = command.id();

        Optional.of(userId)
                .filter(id -> !existsById(id))
                .map(id -> createUser(command))
                .ifPresentOrElse(
                        user -> {
                            userRepository.save(user);
                            logger.info("User registered: {}", user.getId());
                        },
                        () -> {
                            throw userAlreadyExistsError(userId);
                        }
                );
    }

    private User createUser(RegisterUserCommand command) {
        return User.create(command.id(), command.password(), command.email(), command.nickname());
    }

    @Override
    public LoginUserResult login(LoginUserCommand command) {
        return userRepository.findById(command.id())
                .map(user -> validatePassword(user, command.password()))
                .map(user -> {
                    String token = jwtUtil.generateToken(user.getId());
                    logger.info("User logged in: {}", user.getId());
                    return new LoginUserResult(token);
                })
                .orElseThrow(() -> userLoginFailedError(command.id(), "not found"));
    }

    private User validatePassword(User user, String password) {
        return Optional.of(user)
                .filter(u -> PasswordUtil.checkPassword(password, u.getPassword()))
                .orElseThrow(() -> userLoginFailedError(user.getId(), "password mismatch"));
    }

    @Override
    public boolean existsById(String userId) {
        return userRepository.findById(userId).isPresent();
    }
}