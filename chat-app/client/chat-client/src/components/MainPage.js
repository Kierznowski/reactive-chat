import './../styles/MainPage.css';
import RoomList from "./RoomList";
import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

let userName = "";
let userId = "";

export default function MainPage() {
  const [auth, setAuth] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    checkAuth().then(data => {
      console.log("Auth:", data);
      setAuth(data.authenticated);
      userName = data.username;
      userId = data.userid;
      console.log(data);
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


  if (auth === null) return <div>Loading...</div>;

  return auth ? 
    (
        <div id='main-container'>
            <RoomList username={userName} userId={userId} />
        </div>
    ) :
    (

    <div id='main-container'>
        <div className='title'>
            <h1 id="header">You are not logged in</h1>
        </div>
        <div className="buttons">
            <button className="select-button" onClick={() => 
                window.location.href = "http://localhost:9000/oauth2/authorization/chat_auth_server"
            }>
            Sign up
            </ button>
            <button className="select-button" onClick={() => navigate("/register")}>
                Register new account
            </button>
        </div>
    </div>
    )
}