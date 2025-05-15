package com.workintech.twitter.repository;

import com.workintech.twitter.entity.Retweet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RetweetRepository extends JpaRepository<Retweet, Long> {
    List<Retweet> findByUser_UserId(Long userId);
    List<Retweet> findByTweet_TweetId(Long tweetId);
    boolean existsByUser_UserIdAndTweet_TweetId(Long userId, Long tweetId);
    int countByTweet_TweetId(Long tweetId);
}
