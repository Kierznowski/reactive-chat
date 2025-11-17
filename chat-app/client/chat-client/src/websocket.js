let socket;

export function connect(userId, roomId, onMessage) {
    socket = new WebSocket("ws://localhost:9000/chat-message");

    socket.onopen = () => {
        socket.send(JSON.stringify({
            type: "JOIN",
            senderId: userId,
            roomId: roomId
        }));
    };

    socket.onmessage = event => {
        onMessage(JSON.parse(event.data));
    };
}

export function sendMessage(userId, roomId, content) {
    if(!socket || socket.readyState !== WebSocket.OPEN) return;

    socket.send(JSON.stringify({
        type: "MESSAGE",
        senderId: userId,
        roomId: roomId,
        content: content
    }));
}

export function closeSocket() {
    if (socket) socket.close();
}