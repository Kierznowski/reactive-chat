import { useEffect, useState } from "react";
import RoomList from "./components/RoomList";

export default function App() {
  const [auth, setAuth] = useState(null);
  useEffect(() => {
    checkAuth().then(data => {
      console.log("Auth:", data);
      setAuth(data.authenticated);
    });
  }, []);
  
  async function checkAuth() {
    const res = await fetch("http://localhost:9000/gateway/auth/status", {
      credentials: "include"
    });

    if (!res.ok) {
      throw new Error("Request failed");
    }

    return await res.json();
  }


  if (auth === null) return <div>Loading...</div>

  return (
    auth ? 
    (
      <div>
        <RoomList />
      </div>
    ) :
    (
      <div>
        <h1>You are not logged in</h1>
        <button onClick={() => 
          window.location.href = "http://localhost:9000/oauth2/authorization/chat_auth_server"
        }>
          Zaloguj
        </ button>
      </div>
    )
  );
}