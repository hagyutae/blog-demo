package com.sb02.blogdemo.core.user.usecase;

import com.sb02.blogdemo.auth.JwtUtil;
import com.sb02.blogdemo.core.user.PasswordUtil;
import com.sb02.blogdemo.core.user.entity.User;
import com.sb02.blogdemo.core.user.exception.UserAlreadyExists;
import com.sb02.blogdemo.core.user.exception.UserLoginFailed;
import com.sb02.blogdemo.core.user.port.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    public static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepositoryPort userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public void registerUser(RegisterUserCommand command) {
        if (existsById(command.id())) {
            String errMsg = "User with id " + command.id() + " already exists";
            logger.error(errMsg);
            throw new UserAlreadyExists(errMsg);
        }

        User user = User.create(command.id(), command.password(), command.email(), command.nickname());
        userRepository.save(user);
    }

    @Override
    public LoginUserResult login(LoginUserCommand command) {
        Optional<User> found = userRepository.findById(command.id());
        if (found.isEmpty()) {
            String errMsg = "User with id " + command.id() + " not found";
            logger.error(errMsg);
            throw new UserLoginFailed(errMsg);
        }

        User user = found.get();

        boolean matched = PasswordUtil.checkPassword(command.password(), user.getPassword());

        if (!matched) {
            String errMsg = "Password mismatch for user with id " + command.id();
            logger.error(errMsg);
            throw new UserLoginFailed(errMsg);
        }

        String token = jwtUtil.generateToken(user.getId());

        return new LoginUserResult(token);
    }

    @Override
    public boolean existsById(String userId) {
        Optional<User> found = userRepository.findById(userId);
        return found.isPresent();
    }
}
