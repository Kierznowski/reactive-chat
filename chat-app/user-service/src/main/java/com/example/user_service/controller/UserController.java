package com.example.user_service.controller;

import com.example.user_service.model.Room;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping("/login")
    public void loginUser() {
    }

    @PostMapping("/register")
    public void registerUser() {
    }
}
