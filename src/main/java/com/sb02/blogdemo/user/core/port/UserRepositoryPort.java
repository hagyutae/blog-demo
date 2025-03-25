package com.sb02.blogdemo.user.core.port;

import com.sb02.blogdemo.user.core.entity.User;

import java.util.Optional;

public interface UserRepositoryPort {
    void save(User user);
    Optional<User> findById(String userId);
}
