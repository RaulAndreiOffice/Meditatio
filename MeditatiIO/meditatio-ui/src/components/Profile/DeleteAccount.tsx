import React from 'react';
import axios from 'axios';

const DeleteAccount: React.FC = () => {
    const userId = localStorage.getItem('userId');
    const token = localStorage.getItem('token');

    const handleDelete = async () => {

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
                // Aici poți adăuga logica de a deloga utilizatorul și a-l redirecționa
                localStorage.clear();
                window.location.href = '/login'; // Redirecționare
            } catch (error) {
                console.error('Eroare la ștergere:', error);
                alert('Eroare la ștergerea contului!');
            }
        }
    };

    return (
        <div>

            <p>Atenție: Această acțiune este ireversibilă și va șterge permanent contul.</p>
            <button onClick={handleDelete} className="delete-button">
                Șterge Contul Permanent
            </button>
        </div>
    );
};

export default DeleteAccount;