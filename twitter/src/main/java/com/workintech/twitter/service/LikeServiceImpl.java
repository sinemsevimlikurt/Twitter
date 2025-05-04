package com.workintech.twitter.service;

import com.workintech.twitter.entity.Like;
import com.workintech.twitter.entity.LikeId;
import com.workintech.twitter.entity.Tweet;
import com.workintech.twitter.entity.User;
import com.workintech.twitter.repository.LikeRepository;
import com.workintech.twitter.repository.TweetRepository;
import com.workintech.twitter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeServiceImpl implements LikeService {

    @Override
    public List<Like> findLikesByTweetId(Long tweetId) {
        return List.of();
    }

    @Override
    public List<Like> findLikesByUserId(Long userId) {
        return List.of();
    }

    @Override
    public boolean likeExists(Long userId, Long tweetId) {
        return false;
    }

    @Override
    public void likeTweet(Long userId, Long tweetId) {

    }

    @Override
    public void unlikeTweet(Long userId, Long tweetId) {

    }
}
