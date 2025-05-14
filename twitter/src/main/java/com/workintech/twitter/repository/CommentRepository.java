package com.workintech.twitter.repository;

import com.workintech.twitter.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByTweet_TweetId(Long tweetId);
    List<Comment> findByUser_UserId(Long userId);
}
