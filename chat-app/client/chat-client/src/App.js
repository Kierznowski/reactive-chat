import { useState } from "react";
import Login from "./components/Login";
import ChatRoom from "./components/ChatRoom";

export default function App() {
  const [user, setUser] = useState(null);

  const join = (userId, roomId) => {
    setUser({ userId, roomId });
  }

  return (
    <div>
      {user ? (
        <ChatRoom userId={user.userId} roomId={user.roomId} />
      ) : (
        <Login onJoin={join} />
      )
    }
    </div>
  );
}