package com.example.user_service.controller;

import com.example.user_service.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Mono;

@Controller("/user")
public class UserController {


    @PostMapping("/login")
    public Mono<User> loginUser() {
        return Mono.just(null);
    }

    @PostMapping("/register")
    public Mono<User> registerUser() {
        return Mono.just(null);
    }
}
