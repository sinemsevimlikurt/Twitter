package com.workintech.twitter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserDTO(
    Long userId,
    
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    String username,
    
    String createdAt
) {
    // Records automatically generate constructors, getters, equals, hashCode, and toString
    // They are immutable by default
    
    // Default constructor for deserialization
    public UserDTO() {
        this(null, "", "");
    }
}
