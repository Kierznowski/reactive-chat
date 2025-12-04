package com.example.gateway.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketSession;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionRegistry {

    private Map<String, WebSocketSession> users = new ConcurrentHashMap<>();
    private Map<String, Set<WebSocketSession>> rooms = new ConcurrentHashMap<>();
    private Map<WebSocketSession, String> sessionUser = new ConcurrentHashMap<>();
    private Map<WebSocketSession, String> sessionRoom = new ConcurrentHashMap<>();

    public void addUser(String userId, WebSocketSession webSocketSession) {
        WebSocketSession oldSession = users.get(userId);
        if(oldSession != null && oldSession.isOpen()) {
            removeSession(oldSession);
        }

        users.put(userId, webSocketSession);
        sessionUser.put(webSocketSession, userId);
    }

    public void joinRoom(String roomId, WebSocketSession session) {
        rooms.computeIfAbsent(roomId, k -> ConcurrentHashMap.newKeySet()).add(session);
        sessionRoom.put(session, roomId);
    }

    public Set<WebSocketSession> getSessionsInRoom(String roomId) {
        return rooms.getOrDefault(roomId, Collections.emptySet());
    }

    public void removeSession(WebSocketSession session) {
        String user = sessionUser.remove(session);
        String room = sessionRoom.remove(session);
        if (user != null) {
            users.remove(user);
        }

        if (room != null) {
            Set<WebSocketSession> sessions = rooms.get(room);
            if (sessions != null) {
                sessions.remove(session);
                if(sessions.isEmpty()) rooms.remove(room);
            }
        }

    }
}
