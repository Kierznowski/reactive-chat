package com.example.user_service.mapper;

import com.example.user_service.DTO.RoomDTO;
import com.example.user_service.model.Room;
import com.example.user_service.model.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoomMapper {

    public RoomDTO toDto(Room room) {
        List<Long> memberIds = room.getMembers().stream()
                .map(User::getId)
                .toList();
        return new RoomDTO(
                room.getId(),
                room.getName(),
                room.getOwner().getId(),
                memberIds
        );
    }

}
