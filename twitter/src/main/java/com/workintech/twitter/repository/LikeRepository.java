package com.workintech.twitter.repository;

import com.workintech.twitter.entity.Like;
import com.workintech.twitter.entity.LikeId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, LikeId> {
    List<Like> findByTweetId(Long tweetId);
    List<Like> findByUserId(Long userId);
    boolean existsByUserIdAndTweetId(Long userId, Long tweetId);
}
