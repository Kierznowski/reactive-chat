package com.example.user_service.model;

import org.springframework.data.annotation.Id;

import java.util.List;

public class User {

    @Id
    String id;
    String email;
    String passwordHash;
    List<String> roomIds;
}
