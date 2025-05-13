package com.workintech.twitter.service;

import com.workintech.twitter.dto.RetweetDTO;

import java.util.List;

public interface RetweetService {
    RetweetDTO createRetweet(Long userId, Long tweetId);
    List<RetweetDTO> findRetweetsByUserId(Long userId);
    void deleteRetweet(Long retweetId, Long currentUserId);
    boolean retweetExists(Long userId, Long tweetId);
}
