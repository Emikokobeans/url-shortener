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
    @DisplayName("should return hash with the correct length")
    void shouldReturnAliasHash() {
        ShortenUrlResponse response = service.shorten("https://example.com/some/long/path");

        assertNotNull(response.hash());
        assertEquals(6, response.hash().length());
    }

    @Test
    @DisplayName("should return a url with the baseUrl set")
    void shouldReturnShortenedUrl() {
        ShortenUrlResponse response = service.shorten("https://example.com/some/long/path");

        assertTrue(response.shortenedUrl().startsWith("http://localhost:8080/"));
    }

    @Test
    @DisplayName("should return the original inputted url")
    void shouldReturnInputtedUrl() {
        ShortenUrlResponse response = service.shorten("https://example.com/some/long/path");

        assertEquals("https://example.com/some/long/path", response.inputUrl());
    }

    @Test
    @DisplayName("should store inputted url so it can be retrieved")
    void shouldStoreUrlSoItCanBeResolved() {
        ShortenUrlResponse response = service.shorten("https://example.com");

        String resolved = service.resolve(response.hash());

        assertEquals("https://example.com", resolved);
    }

    @Test
    @DisplayName("should throw an error when the inputted url is invalid")
    void shouldRejectInvalidUrl() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.shorten("not-a-url")
        );

        assertEquals("Invalid URL", ex.getMessage());
    }

    @Test
    @DisplayName("should throw an error when the inputted url is not a http or https url")
    void shouldRejectNonHttpUrl() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.shorten("ftp://example.com/file")
        );

        assertEquals("Only http and https URLs are allowed", ex.getMessage());
    }

    @Test
    @DisplayName("should return null when the hash does not exist")
    void shouldReturnNullForUnknownHash() {
        assertNull(service.resolve("unknown"));
    }
}