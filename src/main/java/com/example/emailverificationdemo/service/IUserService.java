package com.example.emailverificationdemo.service;

import com.example.emailverificationdemo.registration.RegistrationRequest;
import com.example.emailverificationdemo.registration.token.VerificationToken;
import com.example.emailverificationdemo.user.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<User> getUsers();
    User registration(RegistrationRequest request);
    Optional<User> findByEmail(String email);

    void saveUserVerificationToken(org.apache.catalina.User theUser, String verificationToken);

    String validateToken(String theToken);

}
