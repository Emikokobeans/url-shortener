package com.ewong.urlshortener.controller;

import com.ewong.urlshortener.dto.ShortenUrlRequest;
import com.ewong.urlshortener.dto.ShortenUrlResponse;
import com.ewong.urlshortener.service.UrlShortenerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/urls")
public class UrlShortenerController {

    private final UrlShortenerService urlShortenerService;

    public UrlShortenerController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    @PostMapping
    public ResponseEntity<ShortenUrlResponse> shorten(@Valid @RequestBody ShortenUrlRequest request) {
        return ResponseEntity.ok(urlShortenerService.shorten(request.inputUrl()));
    }
}