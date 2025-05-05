package com.workintech.twitter.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeId implements Serializable {
    private Long user;
    private Long tweet;

    public Long getUser() {
        return user;
    }

    public Long getTweet() {
        return tweet;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public void setTweet(Long tweet) {
        this.tweet = tweet;
    }
}
