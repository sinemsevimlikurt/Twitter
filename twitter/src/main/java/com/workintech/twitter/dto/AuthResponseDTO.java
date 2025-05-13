package com.workintech.twitter.dto;

public record AuthResponseDTO(
    String token,
    String username,
    Long userId
) {
    // Default constructor for deserialization
    public AuthResponseDTO() {
        this("", "", null);
    }
}
