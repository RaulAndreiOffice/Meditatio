import React, { useState } from 'react';
import axios from 'axios';
import './Login.css'
import { useNavigate } from "react-router-dom";
import VideoBackground from "../VideoBackground/VideoBackground";



    interface LoginProps {
        onLoginSuccess: (username: string , role: string) => void;
    }

    const Login: React.FC<LoginProps> = ({ onLoginSuccess }) => {
        const [username, setUsername] = useState('');
        const [password, setPassword] = useState('');
        const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            const response = await axios.post('http://localhost:9090/api/auth/login', {username, password});
            // Salvează token-ul JWT
            const {token, userId, username: loggedInUsername, roles} = response.data;
            if (!roles || roles.length == 0) {
                alert('Utilizatorul nu are niciun rol atribuit!');
            }
            const userRole = roles[0];

            // Salvam in localStore
            localStorage.setItem('token', token);
            localStorage.setItem('username', loggedInUsername);
            localStorage.setItem('userId', userId.toString());
            localStorage.setItem('role', userRole);

            onLoginSuccess(loggedInUsername, userRole);

            alert('Autentificare reușită!');

            if (userRole == 'ROLE_PROFESOR') {
                navigate('/professor');
            } else if (userRole == 'ROLE_STUDENT') {
                navigate('/student');
            } else {
                navigate('/profile');
            }
        } catch (error) {
            console.error('Eroare la autkentificare: ', error);
            alert('Eroare la autentificare!');
        }
    };

    return (
        <div className="login-page">
            <VideoBackground/>
            <div className="login-container">
                <form onSubmit={handleSubmit}>
                    <h2>Login</h2>
                    <div className="form-group">
                        <label htmlFor="username">Username</label>
                        <input
                            type="text"
                            id="username"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="password">Password</label>
                        <input
                            type="password"
                            id="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </div>
                    <button type="submit" className="login-button">Login</button>
                </form>
            </div>
        </div>
    );
};


export default Login;