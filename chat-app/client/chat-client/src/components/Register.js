import { useState } from "react";
import "./../styles/Register.css";
import { useNavigate } from "react-router-dom";

export default function Register() {
    
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [username, setUsername] = useState("");
    const [registered, setRegistered] = useState(false);

    const navigate = useNavigate();

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
                }),
            });

            if(!response.ok) {
                throw new Error(`Response status: ${response.status}`);
            }
            setRegistered(true);
            startRedirection();
        } catch (error) {
            console.log(error);
        }
    }

    function startRedirection() {
        setTimeout(() => navigate('/'), 2000);
    }

    return (
        registered ?
        <div className="registration-container">
            <div className="registered-info">
                You are successfully registered! :)
            </div>
        </div>
        :
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