package com.workintech.twitter.dto;

import java.time.LocalDateTime;

public record UserResponseDTO(
    Long userId,
    String username,
    String email,
    LocalDateTime createdAt
) {
    // Default constructor for deserialization
    public UserResponseDTO() {
        this(null, "", "", null);
    }
}
