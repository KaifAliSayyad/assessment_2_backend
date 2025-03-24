package com.example.register.repos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.register.models.User;

public interface UserRepo extends JpaRepository<User, Integer> {
    Optional<User> findByName(String name);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}
