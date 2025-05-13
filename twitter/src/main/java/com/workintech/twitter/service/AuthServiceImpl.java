package com.workintech.twitter.service;

import com.workintech.twitter.dto.AuthResponseDTO;
import com.workintech.twitter.dto.LoginRequestDTO;
import com.workintech.twitter.dto.UserResponseDTO;
import com.workintech.twitter.entity.User;
import com.workintech.twitter.exception.TwitterException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @Override
    public AuthResponseDTO login(LoginRequestDTO loginRequestDTO) {
        log.info("Authenticating user: {}", loginRequestDTO.username());
        
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDTO.username(), 
                        loginRequestDTO.password()
                )
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        User user = userService.findByUsername(loginRequestDTO.username());
        
        log.info("User authenticated successfully: {}", loginRequestDTO.username());
        return new AuthResponseDTO("authenticated", user.getUsername(), user.getUserId());
    }

    @Override
    public UserResponseDTO getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new TwitterException("User not authenticated");
        }
        
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        
        if (user == null) {
            throw new TwitterException("User not found");
        }
        
        return new UserResponseDTO(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }
}
