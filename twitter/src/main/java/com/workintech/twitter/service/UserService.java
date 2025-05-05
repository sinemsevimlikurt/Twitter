package com.workintech.twitter.service;

import com.workintech.twitter.entity.User;

import java.util.List;

public interface UserService {
    User findById(Long id);
    User findByUsername(String username);
    List<User> findAll();
    User createUser(User user);
    void deleteUser(Long id);
}
