package com.workintech.twitter.service;

import com.workintech.twitter.entity.Like;
import com.workintech.twitter.entity.LikeId;
import com.workintech.twitter.entity.User;
import com.workintech.twitter.entity.Tweet;
import com.workintech.twitter.repository.LikeRepository;
import com.workintech.twitter.repository.UserRepository;
import com.workintech.twitter.repository.TweetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final TweetRepository tweetRepository;

    public LikeServiceImpl(LikeRepository likeRepository,
                           UserRepository userRepository,
                           TweetRepository tweetRepository) {
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.tweetRepository = tweetRepository;
    }

    @Override
    public List<Like> findLikesByTweetId(Long tweetId) {
        return likeRepository.findByTweet_TweetId(tweetId);
    }

    @Override
    public List<Like> findLikesByUserId(Long userId) {
        return likeRepository.findByUser_UserId(userId);
    }

    @Override
    public boolean likeExists(Long userId, Long tweetId) {
        return likeRepository.existsByUser_UserIdAndTweet_TweetId(userId, tweetId);
    }

    @Override
    public void likeTweet(Long userId, Long tweetId) {
        if (!likeExists(userId, tweetId)) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Tweet tweet = tweetRepository.findById(tweetId)
                    .orElseThrow(() -> new RuntimeException("Tweet not found"));

            Like like = new Like();
            like.setUser(user);
            like.setTweet(tweet);
            likeRepository.save(like);
        }
    }

    @Override
    public void unlikeTweet(Long userId, Long tweetId) {
        if (likeExists(userId, tweetId)) {
            LikeId likeId = new LikeId(userId, tweetId);
            likeRepository.deleteById(likeId);
        }
    }
}
