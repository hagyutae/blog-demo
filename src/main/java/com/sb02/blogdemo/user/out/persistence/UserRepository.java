package com.sb02.blogdemo.user.out.persistence;

import com.sb02.blogdemo.user.domain.entity.User;

import java.util.Optional;

public interface UserRepository {
    void save(User user);
    Optional<User> findById(String userId);
}
