package com.example.social.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PostRequest(
        @NotNull(message = "profileId is required")
        Long profileId,
        @NotBlank(message = "content is required")
        @Size(max = 1000, message = "content must be <= 1000 chars")
        String content) {
}
