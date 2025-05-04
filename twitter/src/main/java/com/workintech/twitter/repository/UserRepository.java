package com.workintech.twitter.repository;

import com.workintech.twitter.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // Örnek ek sorgu: username'e göre kullanıcı bul
    User findByUsername(String username);
}
