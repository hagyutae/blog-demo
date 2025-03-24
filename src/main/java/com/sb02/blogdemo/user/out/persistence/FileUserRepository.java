package com.sb02.blogdemo.user.out.persistence;

import com.sb02.blogdemo.user.domain.entity.User;
import com.sb02.blogdemo.user.domain.exception.UserDataFileLost;
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
public class FileUserRepository implements UserRepository {

    private final Path dataPath;
    private final Map<String, User> data;

    public FileUserRepository(@Value("${file.entity}") String entityDir) {
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
                throw new UserDataFileLost("Cannot access user data directory: " + e);
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
            return (ConcurrentHashMap<String, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new UserDataFileLost("Cannot read user data: " + e);
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
            throw new UserDataFileLost("Cannot save user data: " + e);
        }
    }

    @Override
    public Optional<User> findById(String userId) {
        return Optional.ofNullable(data.get(userId));
    }
}
