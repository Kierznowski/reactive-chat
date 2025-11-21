import { useEffect, useState } from "react"

export default function RoomList() {

    const [rooms, setRooms] = useState([]);

    // useEffect(() => {
    //     async function loadRooms() {
    //         try {
    //             const res = await fetch(`http://localhost:9000/gateway/user/${userId}/rooms`);
                
    //         }
    //     }

    // }, []);


    return (
        <div>
            Available rooms:

            Create new room:
            <input 
                placeholder="room name"
                name=""
            />

        </div>
    )    
}