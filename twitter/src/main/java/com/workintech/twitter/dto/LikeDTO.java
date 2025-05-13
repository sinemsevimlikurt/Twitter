package com.workintech.twitter.dto;

public record LikeDTO(
    Long userId,
    Long tweetId,
    String username
) {
    // Records automatically generate constructors, getters, equals, hashCode, and toString
    // They are immutable by default
    
    // Default constructor for deserialization
    public LikeDTO() {
        this(null, null, "");
    }
}
