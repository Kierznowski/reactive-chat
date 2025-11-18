package com.example.user_service.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;
import java.util.UUID;

@Table("chat_room")
public class Room {

    @Id
    UUID id;
    String name;
    String ownerId;
    List<String> memberIds;
}
