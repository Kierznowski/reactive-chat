import { useState } from "react";
import "./../styles/Register.css";

export default function Register() {
    
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [username, setUsername] = useState("");

    async function createAccount() {
        try {
            const response = await fetch(`http://localhost:9000/gateway/auth/register`, {
                method: "POST",
                headers: {
                  "Content-Type": "application/json"  
                },
                body: JSON.stringify({
                    "email": email,
                    "password": password,
                    "username": username
                })
            });

            if(!response.ok) {
                throw new Error(`Response status: ${response.status}`);
            }
        } catch (error) {
            console.log(error);
        }
    }

    return (
        <div className="registration-container">
            <div className="register-title">
                Create new account
            </div>
            
            <div className="register-form">
                
                <input
                    placeholder="e-mail"
                    value={email}
                    className="register-form-input"
                    onChange={(e) => setEmail(e.target.value)}
                />
                <input
                    placeholder="password"
                    value={password}
                    type="password"
                    className="register-form-input"
                    onChange={(e) => setPassword(e.target.value)}
                />
                <input
                    placeholder="username"
                    value={username}
                    className="register-form-input"
                    onChange={(e) => setUsername(e.target.value)}
                />
                <button 
                    className="select-button"
                    onClick={() => createAccount()}>
                    Create
                </button>
            </div>
              
        </div>
    );

}