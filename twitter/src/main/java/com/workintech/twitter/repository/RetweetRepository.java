package com.workintech.twitter.repository;

import com.workintech.twitter.entity.Retweet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RetweetRepository extends JpaRepository<Retweet, Long> {
    List<Retweet> findByUserId(Long userId);
    List<Retweet> findByTweetId(Long tweetId);
    boolean existsByUserIdAndTweetId(Long userId, Long tweetId);
}
