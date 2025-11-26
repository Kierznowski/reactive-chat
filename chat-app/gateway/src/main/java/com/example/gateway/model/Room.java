package com.example.gateway.model;

import lombok.Getter;

import java.util.List;

@Getter
public class Room {
    Long id;
    String name;
    Long ownerId;
    List<Long> memberIds;
}

