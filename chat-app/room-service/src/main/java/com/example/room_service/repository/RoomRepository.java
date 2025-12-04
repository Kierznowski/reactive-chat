package com.example.room_service.repository;

import com.example.room_service.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoomRepository extends JpaRepository<Room, UUID> {

    @Query("select r from Room r where :memberId member of r.members")
    List<Room> findByMemberId(@Param("memberId") UUID memberId);
}
