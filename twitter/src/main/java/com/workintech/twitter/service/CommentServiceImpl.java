package com.workintech.twitter.service;

import com.workintech.twitter.dto.CommentDTO;
import com.workintech.twitter.entity.Comment;
import com.workintech.twitter.entity.Tweet;
import com.workintech.twitter.entity.User;
import com.workintech.twitter.exception.TwitterException;
import com.workintech.twitter.repository.CommentRepository;
import com.workintech.twitter.repository.TweetRepository;
import com.workintech.twitter.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    public CommentServiceImpl(CommentRepository commentRepository, 
                             TweetRepository tweetRepository, 
                             UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CommentDTO createComment(CommentDTO commentDTO) {
        User user = userRepository.findById(commentDTO.getUserId())
                .orElseThrow(() -> new TwitterException("User not found with id: " + commentDTO.getUserId()));

        Tweet tweet = tweetRepository.findById(commentDTO.getTweetId())
                .orElseThrow(() -> new TwitterException("Tweet not found with id: " + commentDTO.getTweetId()));

        Comment comment = new Comment();
        comment.setContent(commentDTO.getContent());
        comment.setUser(user);
        comment.setTweet(tweet);
        comment.setCreatedAt(LocalDateTime.now());

        Comment savedComment = commentRepository.save(comment);

        return convertToDTO(savedComment);
    }

    @Override
    public List<CommentDTO> findCommentsByTweetId(Long tweetId) {
        tweetRepository.findById(tweetId)
                .orElseThrow(() -> new TwitterException("Tweet not found with id: " + tweetId));

        List<Comment> comments = commentRepository.findByTweetId(tweetId);
        return comments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDTO updateComment(Long commentId, CommentDTO commentDTO, Long currentUserId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new TwitterException("Comment not found with id: " + commentId));

        // Check if the current user is the owner of the comment
        if (!comment.getUser().getUserId().equals(currentUserId)) {
            throw new TwitterException("You are not authorized to update this comment");
        }

        comment.setContent(commentDTO.getContent());
        Comment updatedComment = commentRepository.save(comment);

        return convertToDTO(updatedComment);
    }

    @Override
    public void deleteComment(Long commentId, Long currentUserId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new TwitterException("Comment not found with id: " + commentId));

        // Check if the current user is the owner of the comment or the tweet
        if (!comment.getUser().getUserId().equals(currentUserId) && 
            !comment.getTweet().getUser().getUserId().equals(currentUserId)) {
            throw new TwitterException("You are not authorized to delete this comment");
        }

        commentRepository.delete(comment);
    }

    private CommentDTO convertToDTO(Comment comment) {
        return new CommentDTO(
            comment.getCommentId(),
            comment.getContent(),
            comment.getTweet().getTweetId(),
            comment.getUser().getUserId(),
            comment.getUser().getUsername()
        );
    }
}
