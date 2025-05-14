package com.workintech.twitter.service;

import com.workintech.twitter.dto.TweetDTO;
import com.workintech.twitter.entity.Tweet;
import com.workintech.twitter.entity.User;
import com.workintech.twitter.exception.TwitterException;
import com.workintech.twitter.repository.TweetRepository;
import com.workintech.twitter.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TweetServiceImplTest {

    @Mock
    private TweetRepository tweetRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TweetServiceImpl tweetService;

    private User testUser;
    private Tweet testTweet;
    private TweetDTO testTweetDTO;

    @BeforeEach
    void setUp() {
        // Setup test user
        testUser = new User();
        testUser.setUserId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");
        testUser.setCreatedAt(LocalDateTime.now());

        // Setup test tweet
        testTweet = new Tweet();
        testTweet.setTweetId(1L);
        testTweet.setContent("Test tweet content");
        testTweet.setUser(testUser);
        testTweet.setCreatedAt(LocalDateTime.now());

        // Setup test tweet DTO
        testTweetDTO = new TweetDTO(1L, "Test tweet content", 1L, "testuser");
    }

    @Test
    void createTweet_Success() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(tweetRepository.save(any(Tweet.class))).thenReturn(testTweet);

        // Act
        TweetDTO result = tweetService.createTweet(testTweetDTO);

        // Assert
        assertNotNull(result);
        assertEquals(testTweetDTO.content(), result.content());
        assertEquals(testTweetDTO.userId(), result.userId());
        verify(tweetRepository, times(1)).save(any(Tweet.class));
    }

    @Test
    void createTweet_UserNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TwitterException.class, () -> tweetService.createTweet(testTweetDTO));
        verify(tweetRepository, never()).save(any(Tweet.class));
    }

    @Test
    void findTweetsByUserId_Success() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(tweetRepository.findByUser_UserId(1L)).thenReturn(Arrays.asList(testTweet));

        // Act
        List<TweetDTO> result = tweetService.findTweetsByUserId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testTweetDTO.content(), result.get(0).content());
    }

    @Test
    void findTweetById_Success() {
        // Arrange
        when(tweetRepository.findById(1L)).thenReturn(Optional.of(testTweet));

        // Act
        TweetDTO result = tweetService.findTweetById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testTweetDTO.tweetId(), result.tweetId());
        assertEquals(testTweetDTO.content(), result.content());
    }

    @Test
    void findTweetById_NotFound() {
        // Arrange
        when(tweetRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TwitterException.class, () -> tweetService.findTweetById(1L));
    }

    @Test
    void updateTweet_Success() {
        // Arrange
        when(tweetRepository.findById(1L)).thenReturn(Optional.of(testTweet));
        when(tweetRepository.save(any(Tweet.class))).thenReturn(testTweet);

        // Act
        TweetDTO result = tweetService.updateTweet(1L, testTweetDTO, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(testTweetDTO.content(), result.content());
        verify(tweetRepository, times(1)).save(any(Tweet.class));
    }

    @Test
    void updateTweet_Unauthorized() {
        // Arrange
        when(tweetRepository.findById(1L)).thenReturn(Optional.of(testTweet));

        // Act & Assert
        assertThrows(TwitterException.class, () -> tweetService.updateTweet(1L, testTweetDTO, 2L));
        verify(tweetRepository, never()).save(any(Tweet.class));
    }

    @Test
    void deleteTweet_Success() {
        // Arrange
        when(tweetRepository.findById(1L)).thenReturn(Optional.of(testTweet));

        // Act
        tweetService.deleteTweet(1L, 1L);

        // Assert
        verify(tweetRepository, times(1)).delete(testTweet);
    }

    @Test
    void deleteTweet_Unauthorized() {
        // Arrange
        when(tweetRepository.findById(1L)).thenReturn(Optional.of(testTweet));

        // Act & Assert
        assertThrows(TwitterException.class, () -> tweetService.deleteTweet(1L, 2L));
        verify(tweetRepository, never()).delete(any(Tweet.class));
    }
}
