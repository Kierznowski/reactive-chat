import { useEffect, useState } from "react";
import { sendMessage, connect, closeSocket } from "../websocket";

export default function ChatRoom({ userId, roomId }) {
    const [messages, setMessages] = useState([]);
    const [text, setText] = useState("");

    useEffect(() => {
      async function load() {
        const res = await fetch(`http://localhost:9200/history/${roomId}`);
        const data = await res.json();
        setMessages(data);
      }
      load();
    }, [roomId])

    useEffect(() => {
        connect(userId, roomId, msg => {
            setMessages(prev => [...prev, msg]);
        });

        return () => closeSocket();
    }, [userId, roomId]);

    const send = () => {
        if(text.trim().length === 0) return;
        sendMessage(userId, roomId, text);
        setText("");
    }

    return (
        <div style={styles.container}>
            <h2>Room: {roomId}</h2>

            <div style={styles.messages}>
                {messages.map((m, i) => (
                    <div key={i} style={styles.message}>
                        <b>{m.senderId}: </b>{m.content}
                    </div>
                ))}
            </div>
            <div style={styles.inputRow}>
                <input
                    style={styles.input}
                    value={text}
                    placeholder="Write message..."
                    onChange={(e) => setText(e.target.value)}
                />
                <button style={styles.button} onClick={send}>Send</button>
            </div>

        </div>
    );
}

const styles = {
  container: {
    width: "500px",
    margin: "40px auto",
    display: "flex",
    flexDirection: "column"
  },
  messages: {
    border: "1px solid #ccc",
    padding: "10px",
    height: "300px",
    overflowY: "auto",
    marginBottom: "10px"
  },
  message: {
    padding: "4px 0"
  },
  inputRow: {
    display: "flex"
  },
  input: {
    flex: 1,
    padding: "10px"
  },
  button: {
    padding: "10px"
  }
};