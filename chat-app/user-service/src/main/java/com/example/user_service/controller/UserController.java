package com.example.user_service.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller("/user")
public class UserController {


    @PostMapping("/login")
    public void loginUser() {
    }

    @PostMapping("/register")
    public void registerUser() {
    }
}
