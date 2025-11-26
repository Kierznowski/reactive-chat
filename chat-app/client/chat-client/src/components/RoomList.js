import "./../styles/RoomList.css";

import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

        
export default function RoomList() {

    const [rooms, setRooms] = useState([]);
    const [roomName, setRoomName] = useState("");
    const roomData = {userId: "Przem", roomId: "priv"};

     useEffect(() => {
        async function loadRooms() {
            try {
                const response = await fetch("http://localhost:9000/gateway/rooms", {
                    credentials: "include"
                });
                if(!response.ok) {
                    throw new Error(`Response status: ${response.status}`);
                }

                const result = await response.json();
                setRooms(result);
                console.log("Loaded rooms: " + result[0].name);
            } catch (error) {
                console.log(error.message);
            }
        }
        loadRooms();
     }, []);


    function createRoom() {
        console.log(`room ${roomName} created!`);
    }

    return (
        <div className="main-container">
            <div className="inner-box">
                <div className="user-rooms">
                    Your rooms:
                    {
                        rooms.length === 0 ? 
                        <div className="no-rooms-msg">You have no rooms. Create one!</div>
                        : <ul>
                            {rooms.map(room => 
                                <li key="room.id">
                                    <Link to="/chatRoom" state={{userId: "bill.com", roomId: "general"}} 
                                        className="roomlist-item">
                                            {room.name}
                                    </Link>
                                </li>
                            )}
                        </ul>
                    }
                </div>
                <div className="room-creation">
                    Create new room:
                    <div className="room-input">
                        <input 
                            id="room-input"
                            placeholder="room name"
                            name=""
                        />
                        <button className="select-button" onClick={() => {
                            setRoomName(document.getElementById("room-input").value);
                            createRoom();
                        }}>
                            Create
                        </button>
                    </div>
                </div>

            </div>
        </div>
    )    
}