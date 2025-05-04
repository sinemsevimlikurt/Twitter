package com.workintech.twitter.service;

import com.workintech.twitter.entity.Like;

import java.util.List;

public interface LikeService {
    List<Like> findLikesByTweetId(Long tweetId);
    List<Like> findLikesByUserId(Long userId);
    boolean likeExists(Long userId, Long tweetId);
    void likeTweet(Long userId, Long tweetId);
    void unlikeTweet(Long userId, Long tweetId);
}
