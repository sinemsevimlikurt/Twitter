package com.workintech.twitter.service;

import com.workintech.twitter.dto.UserRequestDTO;
import com.workintech.twitter.dto.UserResponseDTO;
import com.workintech.twitter.entity.User;
import com.workintech.twitter.exception.TwitterException;
import com.workintech.twitter.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with this id: " + id));
    }


    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User createUser(User user) {
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }


    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new TwitterException("User not found with this id: " + id));
        userRepository.delete(user);
    }
    
    @Override
    public UserResponseDTO registerUser(UserRequestDTO userRequestDTO) {
        log.info("Registering new user: {}", userRequestDTO.username());
        
        // Check if username already exists
        if (userRepository.findByUsername(userRequestDTO.username()) != null) {
            throw new TwitterException("Username already exists");
        }
        
        // Create new user
        User user = new User();
        user.setUsername(userRequestDTO.username());
        user.setPassword(passwordEncoder.encode(userRequestDTO.password()));
        user.setEmail(userRequestDTO.email());
        user.setCreatedAt(LocalDateTime.now());
        
        User savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", savedUser.getUsername());
        
        return new UserResponseDTO(
                savedUser.getUserId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getCreatedAt()
        );
    }

}
