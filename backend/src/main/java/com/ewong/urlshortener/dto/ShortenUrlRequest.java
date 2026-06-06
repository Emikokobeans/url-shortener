package com.ewong.urlshortener.dto;

import jakarta.validation.constraints.NotBlank;

public record ShortenUrlRequest(
        @NotBlank(message = "url is required")
        String inputUrl
) {}