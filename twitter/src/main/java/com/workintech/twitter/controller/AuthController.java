package com.workintech.twitter.controller;

import com.workintech.twitter.dto.AuthResponseDTO;
import com.workintech.twitter.dto.LoginRequestDTO;
import com.workintech.twitter.dto.UserRequestDTO;
import com.workintech.twitter.dto.UserResponseDTO;
import com.workintech.twitter.service.AuthService;
import com.workintech.twitter.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        log.info("Registration request received for username: {}", userRequestDTO.username());
        return new ResponseEntity<>(userService.registerUser(userRequestDTO), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        log.info("Login request received for username: {}", loginRequestDTO.username());
        return ResponseEntity.ok(authService.login(loginRequestDTO));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        log.info("Logout request received");
        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/current-user")
    public ResponseEntity<UserResponseDTO> getCurrentUser() {
        log.info("Fetching current user details");
        return ResponseEntity.ok(authService.getCurrentUser());
    }
}
