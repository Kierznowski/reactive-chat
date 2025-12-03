import "./../styles/ChatRoom.css";
import { useEffect, useState } from "react";
import { sendMessage, connect, closeSocket } from "../websocket";
import { useLocation } from "react-router-dom";

export default function ChatRoom() {
    const [messages, setMessages] = useState([]);
    const [text, setText] = useState("");

    const location = useLocation();
    const { userId, roomName, roomId } = location.state;

    useEffect(() => {
      async function load() {
        try {
          const res = await fetch(`http://localhost:9000/gateway/history/${roomId}`, {
            method: "GET",
            credentials: "include"
          });
          if(res.redirected) {
            window.location.href = res.url;
            return;
          }

          if(res.status === 401) {
              window.location.href = "http://localhost:9000/oauth2/authorization/chat_auth_server";
            return;
          }

          const data = await res.json();
          console.log("Fetched history: " + data);
          setMessages(Array.isArray(data) ? data : []);
        } catch (err) {
          console.error("Error fetching history", err);
          setMessages([]);
        }
      }
      load();
    }, [roomId])

    useEffect(() => {
        console.log("UserId: ", userId, "roomId: ", roomId);
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
        <div className="chatroom-container">
            <h1>Room: {roomName}</h1>

            <div className="messages">
                {messages.map((m, i) => (
                    <div key={m.id} className="message">
                        <b>{m.senderUsername}: </b>{m.content}
                    </div>
                ))}
            </div>
            <div className="input-row">
                <input
                    value={text}
                    placeholder="Write message..."
                    onChange={(e) => setText(e.target.value)}
                />
                <button className="send-button" onClick={send}>Send</button>
            </div>

        </div>
    );
}

