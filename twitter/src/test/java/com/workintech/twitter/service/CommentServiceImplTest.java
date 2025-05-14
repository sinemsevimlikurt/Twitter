package com.workintech.twitter.service;

import com.workintech.twitter.dto.CommentDTO;
import com.workintech.twitter.entity.Comment;
import com.workintech.twitter.entity.Tweet;
import com.workintech.twitter.entity.User;
import com.workintech.twitter.exception.TwitterException;
import com.workintech.twitter.repository.CommentRepository;
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
public class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TweetRepository tweetRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    private User testUser;
    private Tweet testTweet;
    private Comment testComment;
    private CommentDTO testCommentDTO;

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

        // Setup test comment
        testComment = new Comment();
        testComment.setCommentId(1L);
        testComment.setContent("Test comment content");
        testComment.setUser(testUser);
        testComment.setTweet(testTweet);
        testComment.setCreatedAt(LocalDateTime.now());

        // Setup test comment DTO
        testCommentDTO = new CommentDTO(1L, "Test comment content", 1L, 1L, "testuser");
    }

    @Test
    void createComment_Success() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(tweetRepository.findById(1L)).thenReturn(Optional.of(testTweet));
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);

        // Act
        CommentDTO result = commentService.createComment(testCommentDTO);

        // Assert
        assertNotNull(result);
        assertEquals(testCommentDTO.content(), result.content());
        assertEquals(testCommentDTO.userId(), result.userId());
        assertEquals(testCommentDTO.tweetId(), result.tweetId());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void createComment_UserNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TwitterException.class, () -> commentService.createComment(testCommentDTO));
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void createComment_TweetNotFound() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(tweetRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TwitterException.class, () -> commentService.createComment(testCommentDTO));
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void findCommentsByTweetId_Success() {
        // Arrange
        when(tweetRepository.findById(1L)).thenReturn(Optional.of(testTweet));
        when(commentRepository.findByTweet_TweetId(1L)).thenReturn(Arrays.asList(testComment));

        // Act
        List<CommentDTO> result = commentService.findCommentsByTweetId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testCommentDTO.content(), result.get(0).content());
    }

    @Test
    void updateComment_Success() {
        // Arrange
        when(commentRepository.findById(1L)).thenReturn(Optional.of(testComment));
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);

        // Act
        CommentDTO result = commentService.updateComment(1L, testCommentDTO, 1L);

        // Assert
        assertNotNull(result);
        assertEquals(testCommentDTO.content(), result.content());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void updateComment_Unauthorized() {
        // Arrange
        when(commentRepository.findById(1L)).thenReturn(Optional.of(testComment));

        // Act & Assert
        assertThrows(TwitterException.class, () -> commentService.updateComment(1L, testCommentDTO, 2L));
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void deleteComment_Success_AsCommentOwner() {
        // Arrange
        when(commentRepository.findById(1L)).thenReturn(Optional.of(testComment));

        // Act
        commentService.deleteComment(1L, 1L);

        // Assert
        verify(commentRepository, times(1)).delete(testComment);
    }

    @Test
    void deleteComment_Success_AsTweetOwner() {
        // Arrange
        User otherUser = new User();
        otherUser.setUserId(2L);
        otherUser.setUsername("otheruser");
        
        Comment comment = new Comment();
        comment.setCommentId(1L);
        comment.setContent("Test comment content");
        comment.setUser(otherUser);
        comment.setTweet(testTweet); // testTweet is owned by testUser (userId=1)
        
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        // Act
        commentService.deleteComment(1L, 1L); // testUser (tweet owner) deleting comment

        // Assert
        verify(commentRepository, times(1)).delete(comment);
    }

    @Test
    void deleteComment_Unauthorized() {
        // Arrange
        User otherUser = new User();
        otherUser.setUserId(2L);
        otherUser.setUsername("otheruser");
        
        User anotherUser = new User();
        anotherUser.setUserId(3L);
        anotherUser.setUsername("anotheruser");
        
        Tweet tweet = new Tweet();
        tweet.setTweetId(1L);
        tweet.setUser(otherUser); // tweet owned by userId=2
        
        Comment comment = new Comment();
        comment.setCommentId(1L);
        comment.setUser(anotherUser); // comment by userId=3
        comment.setTweet(tweet);
        
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        // Act & Assert
        // userId=1 trying to delete a comment that belongs to userId=3 on a tweet by userId=2
        assertThrows(TwitterException.class, () -> commentService.deleteComment(1L, 1L));
        verify(commentRepository, never()).delete(any(Comment.class));
    }
}
