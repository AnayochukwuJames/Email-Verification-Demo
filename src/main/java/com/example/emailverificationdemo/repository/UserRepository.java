package com.example.emailverificationdemo.repository;

import com.example.emailverificationdemo.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
//    @Override
    Optional<User> findByEmail(String email);
}
