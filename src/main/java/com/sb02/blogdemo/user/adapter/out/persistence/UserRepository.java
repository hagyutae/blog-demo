package com.sb02.blogdemo.user.adapter.out.persistence;

import com.sb02.blogdemo.SimpleFileRepository;
import com.sb02.blogdemo.user.core.entity.User;
import com.sb02.blogdemo.user.core.port.UserRepositoryPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository extends SimpleFileRepository<String, User> implements UserRepositoryPort {

    public UserRepository(@Value("${file.entity}") String entityDir) {
        super(entityDir, "user.ser");
    }

    @Override
    public void save(User user) {
        data.put(user.getId(), user);
        saveData();
    }

    @Override
    public Optional<User> findById(String userId) {
        return Optional.ofNullable(data.get(userId));
    }
}
