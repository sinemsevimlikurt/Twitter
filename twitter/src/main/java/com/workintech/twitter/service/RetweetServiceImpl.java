package com.workintech.twitter.service;

import com.workintech.twitter.dto.RetweetDTO;
import com.workintech.twitter.entity.Retweet;
import com.workintech.twitter.entity.Tweet;
import com.workintech.twitter.entity.User;
import com.workintech.twitter.exception.TwitterException;
import com.workintech.twitter.repository.RetweetRepository;
import com.workintech.twitter.repository.TweetRepository;
import com.workintech.twitter.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RetweetServiceImpl implements RetweetService {

    private final RetweetRepository retweetRepository;
    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    public RetweetServiceImpl(RetweetRepository retweetRepository,
                             TweetRepository tweetRepository,
                             UserRepository userRepository) {
        this.retweetRepository = retweetRepository;
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    @Override
    public RetweetDTO createRetweet(Long userId, Long tweetId) {
        // Check if user already retweeted this tweet
        if (retweetExists(userId, tweetId)) {
            throw new TwitterException("You have already retweeted this tweet");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new TwitterException("User not found with id: " + userId));

        Tweet tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new TwitterException("Tweet not found with id: " + tweetId));

        Retweet retweet = new Retweet();
        retweet.setUser(user);
        retweet.setTweet(tweet);
        retweet.setCreatedAt(LocalDateTime.now());

        Retweet savedRetweet = retweetRepository.save(retweet);

        return convertToDTO(savedRetweet);
    }

    @Override
    public List<RetweetDTO> findRetweetsByUserId(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new TwitterException("User not found with id: " + userId));

        List<Retweet> retweets = retweetRepository.findByUserId(userId);
        return retweets.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteRetweet(Long retweetId, Long currentUserId) {
        Retweet retweet = retweetRepository.findById(retweetId)
                .orElseThrow(() -> new TwitterException("Retweet not found with id: " + retweetId));

        // Check if the current user is the owner of the retweet
        if (!retweet.getUser().getUserId().equals(currentUserId)) {
            throw new TwitterException("You are not authorized to delete this retweet");
        }

        retweetRepository.delete(retweet);
    }

    @Override
    public boolean retweetExists(Long userId, Long tweetId) {
        return retweetRepository.existsByUserIdAndTweetId(userId, tweetId);
    }

    private RetweetDTO convertToDTO(Retweet retweet) {
        return new RetweetDTO(
            retweet.getRetweetId(),
            retweet.getTweet().getTweetId(),
            retweet.getUser().getUserId(),
            retweet.getUser().getUsername(),
            retweet.getTweet().getContent()
        );
    }
}
