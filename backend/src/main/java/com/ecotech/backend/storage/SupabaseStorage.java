package com.ecotech.backend.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Service
public class SupabaseStorage implements StorageService {

    private final RestClient restClient;
    private final String bucket;
    private final String baseUrl;
    private final String apiKey;

    public SupabaseStorage(
            @Value("${app.storage.supabase-bucket:reports}") String bucket,
            @Value("${app.storage.supabase-url:#{null}}") String baseUrl,
            @Value("${app.storage.supabase-key:#{null}}") String apiKey) {
        this.bucket = bucket;
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
        this.restClient = RestClient.create();
    }

    @Override
    public String upload(String folder, MultipartFile file) throws IOException {
        if (baseUrl == null || apiKey == null) {
            throw new IllegalStateException("Supabase Storage not configured; set app.storage.supabase-url and app.storage.supabase-key");
        }
        String objectPath = folder + "/" + java.util.UUID.randomUUID() + "_" + sanitize(file.getOriginalFilename());
        String url = baseUrl + "/storage/v1/object/" + bucket + "/" + objectPath;
        try {
            restClient.post()
                    .uri(url)
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Cache-Control", "3600")
                    .contentType(org.springframework.http.MediaType.parseMediaType(file.getContentType()))
                    .body(file.getBytes())
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            throw new IOException("Supabase Storage upload failed: " + e.getMessage(), e);
        }
        return baseUrl + "/storage/v1/object/public/" + bucket + "/" + objectPath;
    }

    @Override
    public InputStream download(String path) throws IOException {
        throw new IOException("Supabase Storage download not implemented");
    }

    @Override
    public void delete(String path) throws IOException {
        throw new IOException("Supabase Storage delete not implemented");
    }

    private static String sanitize(String name) {
        if (name == null) return "file";
        return name.replaceAll("[^a-zA-Z0-9.\\-]", "_");
    }
}
