package com.ewong.urlshortener.service;

import com.ewong.urlshortener.dto.ShortenUrlResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UrlShortenerService {

    private final SecureRandom random = new SecureRandom();
    private final Map<String, String> store = new ConcurrentHashMap<>();

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public ShortenUrlResponse shorten(String inputUrl) {
        validateUrl(inputUrl);

        String hash = generateHash();
        store.put(hash, inputUrl);

        return new ShortenUrlResponse(hash, inputUrl, baseUrl + "/" + hash);
    }

    // To look up the URL that's underneath
    public String resolve(String hash) {
        return store.get(hash);
    }

    private String generateHash() {
        String hash;
        do {
            hash = randomHash();
        } while (store.containsKey(hash));
        return hash;
    }

    private String randomHash() {
        String alphanum = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int hashLength = 6;

        StringBuilder builtHash = new StringBuilder(hashLength);

        for (int i = 0; i < hashLength; i++) {
            builtHash.append(alphanum.charAt(random.nextInt(alphanum.length())));
        }
        return builtHash.toString();
    }

    private void validateUrl(String url) {
        try {
            URI uri = new URI(url);
            if (uri.getScheme() == null || uri.getHost() == null) {
                throw new IllegalArgumentException("Invalid URL");
            }
            if (!uri.getScheme().equalsIgnoreCase("http") && !uri.getScheme().equalsIgnoreCase("https")) {
                throw new IllegalArgumentException("Only http and https URLs are allowed");
            }
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid URL");
        }
    }
}