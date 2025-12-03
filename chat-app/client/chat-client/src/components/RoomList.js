import "./../styles/RoomList.css";

import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

        
export default function RoomList(props) {

    const [rooms, setRooms] = useState([]);
    const [newRoomName, setNewRoomName] = useState("");

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


    async function createRoom() {
        try {
            const response = await fetch(`http://localhost:9000/gateway/rooms/${newRoomName}`, {
                method: "POST",
                credentials: "include"
            });
            if(!response.ok) {
                throw new Error(`Response status: ${response.status}`);
            }
            const result = await response.json();
            setRooms([...rooms, result]);
        } catch (error) {
            console.log(error.message);
        }
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
                                <li key={room.id}>
                                    <Link to="/chatRoom" state={{userId: props.userId, roomName: room.name, roomId: room.id}} 
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
                            onChange={(event) => setNewRoomName(event.target.value)}
                        />
                        <button className="select-button" onClick={() => {
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