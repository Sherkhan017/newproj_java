package com.example.social.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProfileRequest(
        @NotBlank(message = "username is required")
        @Size(max = 100, message = "username must be <= 100 chars")
        String username,
        @Size(max = 500, message = "bio must be <= 500 chars")
        String bio,
        @Size(max = 500, message = "interests must be <= 500 chars")
        String interests) {
}
