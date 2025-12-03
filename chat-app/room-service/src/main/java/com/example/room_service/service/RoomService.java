package com.example.room_service.service;

import com.example.room_service.DTO.RoomDTO;
import com.example.room_service.mapper.RoomMapper;
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
    private final RoomMapper mapper;

    public RoomDTO createRoom(String name, UUID ownerId) {
        Room room = new Room();
        room.setName(name);
        room.setOwnerId(ownerId);
        room.getMembers().add(ownerId);
        return mapper.toDto(repository.save(room));
    }

    public List<RoomDTO> getRoomsForUser(UUID userId) {
        return repository.findByMemberId(userId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public Optional<RoomDTO> getRoom(Long roomId) {
        return repository.findById(roomId).map(mapper::toDto);
    }
}
