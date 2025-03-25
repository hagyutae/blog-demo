package com.sb02.blogdemo.adapter.outbound.user;

import com.sb02.blogdemo.adapter.outbound.SimpleFileRepository;
import com.sb02.blogdemo.core.user.entity.User;
import com.sb02.blogdemo.core.user.port.UserRepositoryPort;
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
