import React, { useState } from "react";
import axios from 'axios';
import "./ProfilePage.css";
import VideoBackground from "../VideoBackground/VideoBackground";


// Am redenumit componenta în ProfilePage pentru claritate
export default function ProfilePage() {
    // State-ul pentru formularul de actualizare (din ProfilePage.tsx)
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    // Logica pentru ȘTERGERE (din DeleteAccount.tsx)
    const handleDelete = async () => {
        const userId = localStorage.getItem('userId');
        const token = localStorage.getItem('token');

        // Avertizarea din DeleteAccount.tsx
        const isConfirmed = window.confirm('Ești sigur că vrei să ștergi contul? Această acțiune este ireversibilă!');

        if (isConfirmed) {
            if (!userId) {
                alert('Trebuie să fii autentificat!');
                return;
            }
            try {
                await axios.delete(`http://localhost:9090/api/users/${userId}`, {
                    headers: {
                        'Authorization': `Bearer ${token}`
                    }
                });
                alert('Contul a fost șters cu succes.');
                localStorage.clear();
                window.location.href = '/login'; // Redirecționare
            } catch (error) {
                console.error('Eroare la ștergere:', error);
                alert('Eroare la ștergerea contului!');
            }
        }
    };

    // Logică pentru ACTUALIZARE (adăugată)
    // Niciun fișier nu avea logica de update, așa că am adăugat-o
    // presupunând un endpoint API similar (ex. PUT sau PATCH)
    const handleUpdate = async () => {
        const userId = localStorage.getItem('userId');
        const token = localStorage.getItem('token');

        if (!userId) {
            alert('Trebuie să fii autentificat!');
            return;
        }

        // Validare simplă
        if (!username && !password) {
            alert('Trebuie să completezi cel puțin un câmp pentru a actualiza.');
            return;
        }

        try {
            // Construim obiectul doar cu datele care trebuie actualizate
            const updateData: { username?: string; password?: string } = {};
            if (username) updateData.username = username;
            if (password) updateData.password = password;

            // Presupunem un endpoint PUT/PATCH pentru actualizare
            await axios.put(`http://localhost:9090/api/users/${userId}`, updateData, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });

            alert('Profilul a fost actualizat cu succes.');
            // Resetăm câmpurile după succes
            setUsername("");
            setPassword("");

        } catch (error) {
            console.error('Eroare la actualizare:', error);
            alert('Eroare la actualizarea profilului!');
        }
    };


    // Structura JSX din ProfilePage.tsx
    return (
        <div className="profile-container">
            <VideoBackground/>
            <div className="profile">
                <h2>Update Account</h2>

                <div className="form-group">
                    <label htmlFor="update-username">New username</label>
                    <input
                        id="update-username"
                        type="text"
                        placeholder="New username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="update-password">New password</label>
                    <input
                        id="update-password"
                        type="password"
                        placeholder="New password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                </div>


                {/* Butonul de actualizare, acum conectat la handleUpdate */}
                <button className="update-button" onClick={handleUpdate}>
                    Actualizează
                </button>

                {/* Butonul de ștergere, acum conectat la handleDelete */}
                <button className="delete-button" style={{marginTop: "10px"}} onClick={handleDelete}>
                    Șterge contul
                </button>

            </div>

        </div>
    );
}