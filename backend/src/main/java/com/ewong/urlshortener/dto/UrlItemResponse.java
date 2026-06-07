package com.ewong.urlshortener.dto;

public record UrlItemResponse(
        String alias,
        String fullUrl,
        String shortenedUrl
) {}