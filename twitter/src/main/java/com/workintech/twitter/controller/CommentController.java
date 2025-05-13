package com.workintech.twitter.controller;

import com.workintech.twitter.dto.CommentDTO;
import com.workintech.twitter.entity.User;
import com.workintech.twitter.service.CommentService;
import com.workintech.twitter.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<CommentDTO> createComment(@Valid @RequestBody CommentDTO commentDTO) {
        // Get current user from security context
        User currentUser = getCurrentUser();
        log.info("Creating comment for tweet {} by user: {}", commentDTO.tweetId(), currentUser.getUsername());
        
        // Set the user ID from the authenticated user
        commentDTO = new CommentDTO(
            commentDTO.commentId(),
            commentDTO.content(),
            commentDTO.tweetId(),
            currentUser.getUserId(),
            currentUser.getUsername()
        );
        
        CommentDTO createdComment = commentService.createComment(commentDTO);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    @GetMapping("/tweet/{tweetId}")
    public ResponseEntity<List<CommentDTO>> getCommentsByTweetId(@PathVariable Long tweetId) {
        List<CommentDTO> comments = commentService.findCommentsByTweetId(tweetId);
        return ResponseEntity.ok(comments);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentDTO> updateComment(
            @PathVariable("id") Long commentId,
            @Valid @RequestBody CommentDTO commentDTO) {
        
        // Get current user from security context
        User currentUser = getCurrentUser();
        log.info("Updating comment {} by user: {}", commentId, currentUser.getUsername());
        
        CommentDTO updatedComment = commentService.updateComment(commentId, commentDTO, currentUser.getUserId());
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable("id") Long commentId) {
        // Get current user from security context
        User currentUser = getCurrentUser();
        log.info("Deleting comment {} by user: {}", commentId, currentUser.getUsername());
        
        commentService.deleteComment(commentId, currentUser.getUserId());
        return ResponseEntity.noContent().build();
    }
    
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        log.debug("Getting current user: {}", username);
        return userService.findByUsername(username);
    }
}
