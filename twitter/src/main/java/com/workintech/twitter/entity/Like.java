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
}
