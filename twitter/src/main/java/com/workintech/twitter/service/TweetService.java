package com.workintech.twitter.service;

import com.workintech.twitter.dto.TweetDTO;
import com.workintech.twitter.entity.Tweet;

import java.util.List;

public interface TweetService {
    TweetDTO createTweet(TweetDTO tweetDTO);
    List<TweetDTO> findTweetsByUserId(Long userId);
    TweetDTO findTweetById(Long tweetId);
    TweetDTO updateTweet(Long tweetId, TweetDTO tweetDTO, Long currentUserId);
    void deleteTweet(Long tweetId, Long currentUserId);
}
