package com.ewong.urlshortener.controller;

import com.ewong.urlshortener.dto.ShortenUrlResponse;
import com.ewong.urlshortener.service.UrlShortenerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UrlShortenerController.class)
class UrlShortenerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UrlShortenerService urlShortenerService;

    @Test
    @DisplayName("should return expected shortened url response")
    void shouldReturnShortenedUrl() throws Exception {
        when(urlShortenerService.shorten(any()))
                .thenReturn(new ShortenUrlResponse("http://localhost:8080/abc1234"));

        mockMvc.perform(post("/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"fullUrl":"https://example.com"}
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.shortenedUrl").value("http://localhost:8080/abc1234"));
    }

    @Test
    @DisplayName("should throw an exception when there is no inputted url")
    void shouldRejectMissingUrl() throws Exception {
        mockMvc.perform(post("/shorten")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"fullUrl":""}
                                """))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("should redirect the alias url to the full url")
    void shouldRedirectAliasUrlToFullUrl() throws Exception {
        when(urlShortenerService.resolve("abc1234")).thenReturn("https://example.com");

        mockMvc.perform(get("/abc1234"))
                .andExpect(status().isFound())
                .andExpect(header().string("Location", "https://example.com"));
    }

    @Test
    @DisplayName("should return a list of past shortened urls")
    void shouldReturnArray() throws Exception {
        when(urlShortenerService.listAll()).thenReturn(java.util.List.of());

        mockMvc.perform(get("/urls"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}
