package com.workintech.twitter.service;

import com.workintech.twitter.dto.TweetDTO;
import com.workintech.twitter.entity.Tweet;
import com.workintech.twitter.entity.User;
import com.workintech.twitter.exception.TwitterException;
import com.workintech.twitter.repository.TweetRepository;
import com.workintech.twitter.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TweetServiceImpl implements TweetService {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    public TweetServiceImpl(TweetRepository tweetRepository, UserRepository userRepository) {
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    @Override
    public TweetDTO createTweet(TweetDTO tweetDTO) {
        User user = userRepository.findById(tweetDTO.userId())
                .orElseThrow(() -> new TwitterException("User not found with id: " + tweetDTO.userId()));

        Tweet tweet = new Tweet();
        tweet.setContent(tweetDTO.content());
        tweet.setUser(user);
        tweet.setCreatedAt(LocalDateTime.now());

        Tweet savedTweet = tweetRepository.save(tweet);

        return convertToDTO(savedTweet);
    }

    @Override
    public List<TweetDTO> findTweetsByUserId(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new TwitterException("User not found with id: " + userId));

        List<Tweet> tweets = tweetRepository.findByUser_UserId(userId);
        return tweets.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<TweetDTO> findAllTweets() {
        List<Tweet> tweets = tweetRepository.findAll();
        return tweets.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<TweetDTO> findTweetsByUsername(String username) {
        // Find the user by username first
        User user = userRepository.findByUsername(username);
        if (user == null) {
            // Return empty list if user not found
            return Collections.emptyList();
        }
        
        // Find tweets by the user's ID
        List<Tweet> tweets = tweetRepository.findByUser_UserId(user.getUserId());
        return tweets.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TweetDTO findTweetById(Long tweetId) {
        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new TwitterException("Tweet not found with id: " + tweetId));

        return convertToDTO(tweet);
    }

    @Override
    public TweetDTO updateTweet(Long tweetId, TweetDTO tweetDTO, Long currentUserId) {
        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new TwitterException("Tweet not found with id: " + tweetId));

        // Check if the current user is the owner of the tweet
        if (!tweet.getUser().getUserId().equals(currentUserId)) {
            throw new TwitterException("You are not authorized to update this tweet");
        }

        tweet.setContent(tweetDTO.content());
        Tweet updatedTweet = tweetRepository.save(tweet);

        return convertToDTO(updatedTweet);
    }

    @Override
    public void deleteTweet(Long tweetId, Long currentUserId) {
        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new TwitterException("Tweet not found with id: " + tweetId));

        // Check if the current user is the owner of the tweet
        if (!tweet.getUser().getUserId().equals(currentUserId)) {
            throw new TwitterException("You are not authorized to delete this tweet");
        }

        tweetRepository.delete(tweet);
    }

    private TweetDTO convertToDTO(Tweet tweet) {
        return new TweetDTO(
                tweet.getTweetId(),
                tweet.getContent(),
                tweet.getUser().getUserId(),
                tweet.getUser().getUsername()
        );
    }
}
