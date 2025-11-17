import { useState } from "react";

export default function Login({ onJoin }) {
    const [userId, setUserId] = useState("");
    const [roomId, setRoomId] = useState("");

    const join = () => {
        if (userId && roomId) {
            onJoin(userId, roomId);
        }
    };

    return (
        <div style={styles.container}>
            <h2>Join chat</h2>

            <input
                style={styles.input}
                placeholder="Username"
                value={userId}
                onChange={e => setUserId(e.target.value)}
            />

            <input
                style={styles.input}
                placeholder="Room name"
                value={roomId}
                onChange={e => setRoomId(e.target.value)}
            />

            <button style={styles.button} onClick={join}>Join</button>
        </div>
    );
}

const styles = {
    container: {
        width: "300px",
        margin: "60px auto",
        display: "flex",
        flexDirection: "column"
    },
    input: {
        padding: "10px",
        marginBottom: "10px"
    },
    button: {
        padding: "10px"
    }
};