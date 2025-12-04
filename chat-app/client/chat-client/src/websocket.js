let socket;

export function connect(userId, roomId, onMessage) {
    if(socket && socket.readyState === WebSocket.OPEN) {
        return;
    }

    socket = new WebSocket("ws://localhost:9000/ws/chat-message");
    socket.onopen = () => {
        socket.send(JSON.stringify({
            type: "JOIN",
            senderId: userId,
            roomId: roomId
        }));
        console.log("Joined userid", userId)
    };

    socket.onmessage = event => {
        try {
            onMessage(JSON.parse(event.data));
        } catch(e) {
            console.error("Invalid ws message:", e);
        }
    };
}

export function sendMessage(userId, roomId, content) {
    if(!socket || socket.readyState !== WebSocket.OPEN) return;

    socket.send(JSON.stringify({
        type: "MESSAGE",
        roomId: roomId,
        senderId: userId,
        content: content
    }));
    console.log("set userid: " + userId);
}

export function closeSocket() {
    console.log("Called --");
    if (socket) socket.close();
}