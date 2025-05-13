package com.workintech.twitter.service;

import com.workintech.twitter.dto.CommentDTO;

import java.util.List;

public interface CommentService {
    CommentDTO createComment(CommentDTO commentDTO);
    List<CommentDTO> findCommentsByTweetId(Long tweetId);
    CommentDTO updateComment(Long commentId, CommentDTO commentDTO, Long currentUserId);
    void deleteComment(Long commentId, Long currentUserId);
}
