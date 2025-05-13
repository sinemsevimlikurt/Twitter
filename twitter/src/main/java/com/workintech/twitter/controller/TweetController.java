package com.workintech.twitter.controller;

import com.workintech.twitter.dto.TweetDTO;
import com.workintech.twitter.entity.User;
import com.workintech.twitter.service.TweetService;
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
@RequestMapping("/api/tweet")
@RequiredArgsConstructor
@Slf4j
public class TweetController {

    private final TweetService tweetService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<TweetDTO> createTweet(@Valid @RequestBody TweetDTO tweetDTO) {
        // Get current user from security context
        User currentUser = getCurrentUser();
        log.info("Creating tweet for user: {}", currentUser.getUsername());
        
        // Set the user ID from the authenticated user
        tweetDTO = new TweetDTO(
            tweetDTO.tweetId(),
            tweetDTO.content(),
            currentUser.getUserId(),
            currentUser.getUsername()
        );
        
        TweetDTO createdTweet = tweetService.createTweet(tweetDTO);
        return new ResponseEntity<>(createdTweet, HttpStatus.CREATED);
    }

    @GetMapping("/findByUserId")
    public ResponseEntity<List<TweetDTO>> findTweetsByUserId(@RequestParam Long userId) {
        List<TweetDTO> tweets = tweetService.findTweetsByUserId(userId);
        return ResponseEntity.ok(tweets);
    }

    @GetMapping("/findById")
    public ResponseEntity<TweetDTO> findTweetById(@RequestParam Long tweetId) {
        TweetDTO tweet = tweetService.findTweetById(tweetId);
        return ResponseEntity.ok(tweet);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TweetDTO> updateTweet(
            @PathVariable("id") Long tweetId,
            @Valid @RequestBody TweetDTO tweetDTO) {
        
        // Get current user from security context
        User currentUser = getCurrentUser();
        log.info("Updating tweet {} for user: {}", tweetId, currentUser.getUsername());
        
        TweetDTO updatedTweet = tweetService.updateTweet(tweetId, tweetDTO, currentUser.getUserId());
        return ResponseEntity.ok(updatedTweet);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTweet(@PathVariable("id") Long tweetId) {
        // Get current user from security context
        User currentUser = getCurrentUser();
        log.info("Deleting tweet {} for user: {}", tweetId, currentUser.getUsername());
        
        tweetService.deleteTweet(tweetId, currentUser.getUserId());
        return ResponseEntity.noContent().build();
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        log.debug("Getting current user: {}", username);
        return userService.findByUsername(username);
    }
}
