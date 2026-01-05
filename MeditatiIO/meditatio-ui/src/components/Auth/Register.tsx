import React, { useState } from 'react';
import axios from 'axios';
import './Register.css';
import {useNavigate} from "react-router-dom";
import VideoBackground from "../VideoBackground/VideoBackground";

const Register: React.FC = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [role, setRole] = useState('ROLE_STUDENT');
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            const registerRequest = {
                username,
                password,
                roleName: role
            }
            await axios.post('http://localhost:9090/api/auth/register', registerRequest);
            alert('Înregistrare reușită!');
            navigate('/login');
        } catch (error) {
            console.error('Eroare la înregistrare:', error);
            alert('Eroare la înregistrare!');
        }
    };

    return (
        <div className="register-page">
            <VideoBackground/>
            <div className="register-container">
                <form onSubmit={handleSubmit}>
                    <h2>Create Account</h2>
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
                    <div className="form-group">
                        <label>Sunt:</label>
                        <div style={{display: 'flex', justifyContent: 'space-around', color: 'white'}}>
                            <label>
                                <input
                                    type="radio"
                                    value="ROLE_STUDENT"
                                    checked={role === 'ROLE_STUDENT'}
                                    onChange={(e) => setRole(e.target.value)}
                                />
                                Student
                            </label>
                            <label>
                                <input
                                    type="radio"
                                    value="ROLE_PROFESOR" // Asigură-te că acest nume ("ROLE_PROFESOR") există în tabela 'roles'
                                    checked={role === 'ROLE_PROFESOR'}
                                    onChange={(e) => setRole(e.target.value)}
                                />
                                Profesor
                            </label>
                        </div>
                    </div>
                    <button type="submit" className="register-button">Register</button>
                </form>
            </div>
        </div>
    );
};

export default Register;
