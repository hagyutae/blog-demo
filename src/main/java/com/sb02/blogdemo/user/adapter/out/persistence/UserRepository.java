package com.sb02.blogdemo.user.adapter.out.persistence;

import com.sb02.blogdemo.exception.LoadDataFileFailure;
import com.sb02.blogdemo.exception.SaveDataFileFailure;
import com.sb02.blogdemo.user.core.port.UserRepositoryPort;
import com.sb02.blogdemo.user.core.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserRepository implements UserRepositoryPort {

    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);

    private final Path dataPath;
    private final Map<String, User> data;

    public UserRepository(@Value("${file.entity}") String entityDir) {
        Path entityDirPath = Paths.get(entityDir);
        createDirectoryIfNotExists(entityDirPath);
        dataPath = entityDirPath.resolve("user.ser");
        data = loadData();
    }

    private void createDirectoryIfNotExists(Path entityDirPath) {
        if (Files.notExists(entityDirPath)) {
            try {
                Files.createDirectories(entityDirPath);
            } catch (IOException e) {
                throw new LoadDataFileFailure("Cannot access user data directory: " + e);
            }
        }
    }

    private ConcurrentHashMap<String, User> loadData() {
        if (!Files.exists(dataPath))
            return new ConcurrentHashMap<>();

        try (
                FileInputStream fis = new FileInputStream(dataPath.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis)
        ) {
            Object obj = ois.readObject();
            if (obj instanceof ConcurrentHashMap<?, ?> map) {

                // 타입 안전성 검증을 위한 새로운 맵 생성
                ConcurrentHashMap<String, User> result = new ConcurrentHashMap<>();

                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    if (entry.getKey() instanceof String && entry.getValue() instanceof User) {
                        result.put((String) entry.getKey(), (User) entry.getValue());
                    } else {
                        String errMsg = "User data file contains invalid types";
                        logger.error(errMsg);
                        throw new LoadDataFileFailure(errMsg);
                    }
                }

                return result;
            } else {
                String errMsg = "User data file is not constructed by a ConcurrentHashMap object";
                logger.error(errMsg);
                throw new LoadDataFileFailure(errMsg);
            }
        } catch (IOException | ClassNotFoundException e) {
            String errMsg = "Cannot load user data: " + e;
            logger.error(errMsg);
            throw new LoadDataFileFailure(errMsg);
        }
    }

    @Override
    public void save(User user) {
        data.put(user.getId(), user);
        saveData();
    }

    private void saveData() {
        try (
                ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(dataPath))
        ) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new SaveDataFileFailure("Cannot save user data: " + e);
        }
    }

    @Override
    public Optional<User> findById(String userId) {
        return Optional.ofNullable(data.get(userId));
    }
}
