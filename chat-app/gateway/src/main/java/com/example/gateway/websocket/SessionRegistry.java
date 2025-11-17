package com.example.gateway.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketSession;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionRegistry {

    private Map<String, WebSocketSession> users = new ConcurrentHashMap<>();
    private Map<String, Set<WebSocketSession>> rooms = new ConcurrentHashMap<>();
    private Map<WebSocketSession, String> sessionUser = new ConcurrentHashMap<>();
    private Map<WebSocketSession, String> sessionRoom = new ConcurrentHashMap<>();

    public void addUser(String userId, WebSocketSession webSocketSession) {
        users.put(userId, webSocketSession);
        sessionUser.put(webSocketSession, userId);
    }

    public void joinRoom(String roomId, WebSocketSession webSocketSession) {
        rooms.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(webSocketSession);
        sessionRoom.put(webSocketSession, roomId);
    }

    public Set<WebSocketSession> getSessionsInRoom(String roomId) {
        return rooms.get(roomId);
    }

    public void removeSession(WebSocketSession webSocketSession) {
        String user = sessionUser.remove(webSocketSession);
        String room = sessionRoom.remove(webSocketSession);
        if (user != null) {
            users.remove(user);
        }

        if (room != null) {
            Set<WebSocketSession> sessions = rooms.get(room);
            if (sessions != null) {
                rooms.get(room).remove(webSocketSession);
            }
        }

    }
}
