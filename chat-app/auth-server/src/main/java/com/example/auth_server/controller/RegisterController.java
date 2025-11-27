package com.example.auth_server.controller;

import com.example.auth_server.DTO.RegisterRequest;
import com.example.auth_server.model.User;
import com.example.auth_server.service.UserService;
import exceptions.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth/register")
public class RegisterController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> registerAccount(@RequestBody RegisterRequest request) {

        try {
            User user = userService.registerUser(request);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "User registered successfully",
                    "userId", user.getId()
            ));
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of(
                            "status", "error",
                            "message", "Registration failed"
                    ));
        }
    }
}
