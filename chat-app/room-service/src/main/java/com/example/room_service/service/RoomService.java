package com.example.room_service.service;

import com.example.room_service.model.Room;
import com.example.room_service.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository repository;

    public Room createRoom(String name, UUID ownerId) {
        Room room = new Room();
        room.setId(UUID.randomUUID());
        room.setName(name);
        room.setOwnerId(ownerId);
        room.getMembers().add(ownerId);
        return repository.save(room);
    }

    public List<Room> getRoomsForUser(UUID userId) {
        return repository.findByMemberId(userId)
                .stream()
                .toList();
    }

    public Optional<Room> getRoom(UUID roomId) {
        return repository.findById(roomId);
    }
}
