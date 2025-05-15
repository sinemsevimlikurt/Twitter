package com.workintech.twitter.repository;

import com.workintech.twitter.entity.Like;
import com.workintech.twitter.entity.LikeId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, LikeId> {
    List<Like> findByTweet_TweetId(Long tweetId);
    List<Like> findByUser_UserId(Long userId);
    boolean existsByUser_UserIdAndTweet_TweetId(Long userId, Long tweetId);
    int countByTweet_TweetId(Long tweetId);
}
