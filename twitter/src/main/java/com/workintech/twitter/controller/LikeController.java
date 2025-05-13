package com.workintech.twitter.controller;

import com.workintech.twitter.dto.LikeDTO;
import com.workintech.twitter.entity.User;
import com.workintech.twitter.service.LikeService;
import com.workintech.twitter.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class LikeController {

    private final LikeService likeService;
    private final UserService userService;

    @PostMapping("/like")
    public ResponseEntity<Void> likeTweet(@RequestBody LikeDTO likeDTO) {
        // Get current user from security context
        User currentUser = getCurrentUser();
        log.info("Liking tweet {} by user: {}", likeDTO.tweetId(), currentUser.getUsername());
        
        likeService.likeTweet(currentUser.getUserId(), likeDTO.tweetId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/dislike")
    public ResponseEntity<Void> unlikeTweet(@RequestBody LikeDTO likeDTO) {
        // Get current user from security context
        User currentUser = getCurrentUser();
        log.info("Unliking tweet {} by user: {}", likeDTO.tweetId(), currentUser.getUsername());
        
        likeService.unlikeTweet(currentUser.getUserId(), likeDTO.tweetId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        log.debug("Getting current user: {}", username);
        return userService.findByUsername(username);
    }
}
