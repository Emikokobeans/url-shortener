package com.ewong.urlshortener.dto;

public record ShortenUrlResponse(
        String hash,
        String inputUrl,
        String shortenedUrl
) {}