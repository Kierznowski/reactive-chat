package com.example.gateway.DTO;

import lombok.Getter;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class RoomDTO {
    Long id;
    String name;
    Long ownerId;
    List<User> memberIds;
}

