package com.workintech.twitter.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentDTO(
    Long commentId,
    
    @NotBlank(message = "Content cannot be blank")
    @Size(min = 1, max = 280, message = "Comment must be between 1 and 280 characters")
    String content,
    
    Long tweetId,
    Long userId,
    String username
) {
    // Records automatically generate constructors, getters, equals, hashCode, and toString
    // They are immutable by default
    
    // Default constructor for deserialization
    public CommentDTO() {
        this(null, "", null, null, "");
    }
}
