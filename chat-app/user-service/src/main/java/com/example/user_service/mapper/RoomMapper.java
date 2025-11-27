package com.example.user_service.mapper;

import com.example.user_service.DTO.RoomDTO;
import com.example.user_service.model.Room;
import com.example.user_service.model.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoomMapper {

    public RoomDTO toDto(Room room) {
        return new RoomDTO(
                room.getId(),
                room.getName(),
                room.getOwner().getId(),
                room.getMembers()
                        .stream()
                        .map(User::getId)
                        .toList()
        );
    }

}
