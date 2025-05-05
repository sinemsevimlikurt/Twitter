package com.workintech.twitter.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "likes")
@IdClass(LikeId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Like {

    @Id
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "tweet_id", nullable = false)
    private Tweet tweet;

    public User getUser() {
        return user;
    }

    public Tweet getTweet() {
        return tweet;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTweet(Tweet tweet) {
        this.tweet = tweet;
    }
}
