package com.workintech.twitter.dto;

public record RetweetDTO(
    Long retweetId,
    Long tweetId,
    Long userId,
    String username,
    String originalTweetContent
) {
    // Records automatically generate constructors, getters, equals, hashCode, and toString
    // They are immutable by default
    
    // Default constructor for deserialization
    public RetweetDTO() {
        this(null, null, null, "", "");
    }
}
