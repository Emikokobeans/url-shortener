package com.ewong.urlshortener.service;

import com.ewong.urlshortener.dto.ShortenUrlResponse;
import com.ewong.urlshortener.dto.UrlItemResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.List;

@Service
public class UrlShortenerService {

    private final SecureRandom random = new SecureRandom();
    private final Map<String, String> store = new LinkedHashMap<>();

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public ShortenUrlResponse shorten(String fullUrl) {
        validateUrl(fullUrl);

        String alias = generateAlias();

        if (store.containsKey(alias)) {
            throw new IllegalArgumentException("Alias already taken");
        }

        store.put(alias, fullUrl);

        return new ShortenUrlResponse(baseUrl + "/" + alias);
    }

    // To look up the URL that's underneath
    public String resolve(String alias) {
        return store.get(alias);
    }

    public boolean delete(String alias) {
        return store.remove(alias) != null;
    }

    private String generateAlias() {
        String alias;
        do {
            alias = randomAlias();
        } while (store.containsKey(alias));
        return alias;
    }

    private String randomAlias() {
        String alphanum = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int aliasLength = 6;

        StringBuilder builtAlias = new StringBuilder(aliasLength);

        for (int i = 0; i < aliasLength; i++) {
            builtAlias.append(alphanum.charAt(random.nextInt(alphanum.length())));
        }
        return builtAlias.toString();
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

     public List<UrlItemResponse> listAll() {
        List<UrlItemResponse> items = new ArrayList<>();
        for (Map.Entry<String, String> entry : store.entrySet()) {
            items.add(new UrlItemResponse(entry.getKey(), entry.getValue(), baseUrl + "/" + entry.getKey()));
        }
        return items;
    }
}