package com.workintech.twitter.controller;

import com.workintech.twitter.dto.RetweetDTO;
import com.workintech.twitter.entity.User;
import com.workintech.twitter.service.RetweetService;
import com.workintech.twitter.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/retweet")
@RequiredArgsConstructor
@Slf4j
public class RetweetController {

    private final RetweetService retweetService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<RetweetDTO> createRetweet(@RequestParam Long tweetId) {
        // Get current user from security context
        User currentUser = getCurrentUser();
        log.info("Retweeting tweet {} by user: {}", tweetId, currentUser.getUsername());
        
        RetweetDTO createdRetweet = retweetService.createRetweet(currentUser.getUserId(), tweetId);
        return new ResponseEntity<>(createdRetweet, HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RetweetDTO>> getRetweetsByUserId(@PathVariable Long userId) {
        log.info("Getting retweets for user: {}", userId);
        List<RetweetDTO> retweets = retweetService.findRetweetsByUserId(userId);
        return ResponseEntity.ok(retweets);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRetweet(@PathVariable("id") Long retweetId) {
        // Get current user from security context
        User currentUser = getCurrentUser();
        log.info("Deleting retweet {} by user: {}", retweetId, currentUser.getUsername());
        
        retweetService.deleteRetweet(retweetId, currentUser.getUserId());
        return ResponseEntity.noContent().build();
    }
    
    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        log.debug("Getting current user: {}", username);
        return userService.findByUsername(username);
    }
}
