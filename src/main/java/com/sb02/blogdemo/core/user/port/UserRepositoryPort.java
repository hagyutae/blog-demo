package com.sb02.blogdemo.core.user.port;

import com.sb02.blogdemo.core.user.entity.User;

import java.util.Optional;

public interface UserRepositoryPort {
    void save(User user);
    Optional<User> findById(String userId);
}
