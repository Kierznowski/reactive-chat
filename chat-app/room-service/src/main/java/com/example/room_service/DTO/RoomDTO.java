package com.example.room_service.DTO;

import java.util.List;
import java.util.UUID;

public record RoomDTO(
        Long id,
        String name,
        UUID ownerId,
        List<UUID> membersIds
) { }
