package com.ecotech.backend.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public interface StorageService {

    String upload(String folder, MultipartFile file) throws IOException;

    InputStream download(String path) throws IOException;

    void delete(String path) throws IOException;
}
