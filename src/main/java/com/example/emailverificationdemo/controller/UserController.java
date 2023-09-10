package com.example.emailverificationdemo.controller;

import com.example.emailverificationdemo.user.User;
import com.example.emailverificationdemo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    @GetMapping("")
    public List<User> getUser() {
        return userService.getUsers();
    }

}
