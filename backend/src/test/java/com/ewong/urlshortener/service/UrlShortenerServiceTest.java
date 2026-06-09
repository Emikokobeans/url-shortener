package com.ewong.urlshortener.service;

import com.ewong.urlshortener.dto.ShortenUrlResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class UrlShortenerServiceTest {

    private UrlShortenerService service;

    @BeforeEach
    void setUp() {
        service = new UrlShortenerService();
        ReflectionTestUtils.setField(service, "baseUrl", "http://localhost:8080");
    }

    @Test
    @DisplayName("should return a url with the baseUrl set")
    void shouldReturnShortenedUrl() {
        ShortenUrlResponse response = service.shorten("https://example.com/some/long/path", null);

        assertTrue(response.shortenedUrl().startsWith("http://localhost:8080/"));
    }

    @Test
    @DisplayName("should return a url with the baseUrl and provided custom alias set")
    void shouldReturnShortenedUrlWithCustomAlias() {
        ShortenUrlResponse response = service.shorten("https://example.com", "custom-alias");

        assertTrue(response.shortenedUrl().contentEquals("http://localhost:8080/custom-alias"));
    }

    @Test
    @DisplayName("should store shortened url so it can be resolved")
    void shouldStoreUrlSoItCanBeResolved() {
        ShortenUrlResponse response = service.shorten("https://example.com", null);
        String alias = response.shortenedUrl().substring(response.shortenedUrl().lastIndexOf('/') + 1);

        assertEquals("https://example.com", service.resolve(alias));
    }

    @Test
    @DisplayName("should throw an error when the inputted url is invalid")
    void shouldRejectInvalidUrl() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.shorten("not-a-url", null)
        );

        assertEquals("Invalid URL", ex.getMessage());
    }

    @Test
    @DisplayName("should throw an error when the inputted url is not a http or https url")
    void shouldRejectNonHttpUrl() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.shorten("ftp://example.com/file", null)
        );

        assertEquals("Only http and https URLs are allowed", ex.getMessage());
    }

    @Test
    @DisplayName("should return null when the alias does not exist")
    void shouldReturnNullForUnknownAlias() {
        assertNull(service.resolve("unknown"));
    }

    @Test
    @DisplayName("should reject a duplicate custom alias")
    void shouldRejectDuplicateCustomAlias() {
        service.shorten("https://example.com", "dummy-alias");

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.shorten("https://example.org", "dummy-alias")
        );

        assertEquals("Alias already taken", ex.getMessage());
    }
}