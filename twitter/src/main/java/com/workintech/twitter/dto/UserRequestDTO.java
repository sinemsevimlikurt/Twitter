package com.workintech.twitter.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequestDTO(
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    String username,
    
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    String password,
    
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    String email
) {
    // Default constructor for deserialization
    public UserRequestDTO() {
        this("", "", "");
    }
}
