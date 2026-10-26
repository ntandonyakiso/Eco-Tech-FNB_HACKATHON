package com.ecotech.backend.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class LocalFileStorage implements StorageService {

    private final Path root;

    public LocalFileStorage(@Value("${app.storage.local-path:./uploads}") String localPath) {
        this.root = Path.of(localPath).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.root);
        } catch (IOException e) {
            throw new IllegalStateException("Cannot create upload directory: " + localPath, e);
        }
    }

    @Override
    public String upload(String folder, MultipartFile file) throws IOException {
        Path dir = root.resolve(folder);
        Files.createDirectories(dir);
        String filename = UUID.randomUUID() + "_" + sanitize(file.getOriginalFilename());
        Path target = dir.resolve(filename);
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        }
        return target.toString();
    }

    @Override
    public InputStream download(String path) throws IOException {
        return new FileInputStream(path);
    }

    @Override
    public void delete(String path) throws IOException {
        Files.deleteIfExists(Path.of(path));
    }

    private static String sanitize(String name) {
        if (name == null) return "file";
        return name.replaceAll("[^a-zA-Z0-9.\\-]", "_");
    }
}
