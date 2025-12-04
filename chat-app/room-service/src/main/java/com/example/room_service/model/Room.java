package com.example.room_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "chat_room")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Room {

    @Id
    private UUID id;

    private String name;

    @Column(name = "owner_id", nullable = false)
    UUID ownerId;

    @ElementCollection
    @CollectionTable(name = "room_member", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "member_id")
    private List<UUID> members = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    private Instant createdAt = Instant.now();
}
