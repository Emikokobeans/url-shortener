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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        when(urlShortenerService.shorten(anyString()))
                .thenReturn(new ShortenUrlResponse(
                        "abc1234",
                        "https://example.com",
                        "http://localhost:8080/abc1234"
                ));

        mockMvc.perform(post("/api/urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"inputUrl":"https://example.com"}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hash").value("abc1234"))
                .andExpect(jsonPath("$.inputUrl").value("https://example.com"))
                .andExpect(jsonPath("$.shortenedUrl").value("http://localhost:8080/abc1234"));
    }

    @Test
    @DisplayName("should throw an exception when there is no inputted url")
    void shouldRejectMissingUrl() throws Exception {
        mockMvc.perform(post("/api/urls")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"inputUrl":""}
                                """))
                .andExpect(status().isBadRequest());
    }
}
