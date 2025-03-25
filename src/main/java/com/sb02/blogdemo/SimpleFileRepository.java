package com.sb02.blogdemo;

import com.sb02.blogdemo.exception.LoadDataFileFailure;
import com.sb02.blogdemo.exception.SaveDataFileFailure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class SimpleFileRepository<K, T> {
    private static final Logger _logger = LoggerFactory.getLogger("FileRepository");

    protected final Path dataPath;
    protected final Map<K, T> data;

    public SimpleFileRepository(String entityDir, String fileName) {
        Path entityDirPath = Paths.get(entityDir);
        createDirectoryIfNotExists(entityDirPath);
        dataPath = entityDirPath.resolve(fileName);
        data = loadData();
    }

    private void createDirectoryIfNotExists(Path entityDirPath) {
        if (Files.notExists(entityDirPath)) {
            try {
                Files.createDirectories(entityDirPath);
            } catch (IOException e) {
                throw new LoadDataFileFailure("Cannot access data directory: " + e);
            }
        }
    }

    private ConcurrentHashMap<K, T> loadData() {
        if (!Files.exists(dataPath))
            return new ConcurrentHashMap<>();

        try (
                FileInputStream fis = new FileInputStream(dataPath.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis)
        ) {
            Object obj = ois.readObject();
            return (ConcurrentHashMap<K, T>) obj;
        } catch (IOException | ClassNotFoundException e) {
            String errMsg = "Cannot load user data: " + e;
            _logger.error(errMsg);
            throw new LoadDataFileFailure(errMsg);
        }
    }

    protected void saveData() {
        try (
                ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(dataPath))
        ) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new SaveDataFileFailure("Cannot save user data: " + e);
        }
    }
}
