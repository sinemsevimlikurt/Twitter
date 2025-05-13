package com.workintech.twitter.service;

import com.workintech.twitter.dto.AuthResponseDTO;
import com.workintech.twitter.dto.LoginRequestDTO;
import com.workintech.twitter.dto.UserResponseDTO;

public interface AuthService {
    AuthResponseDTO login(LoginRequestDTO loginRequestDTO);
    UserResponseDTO getCurrentUser();
}
