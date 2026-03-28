import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from "axios";
 import VideoBackground from "../VideoBackground/VideoBackground";
import './ProfessorPage.css';

import './ProfilePage.css';

interface Assignment {
    assid: number;
    asstitle: string;
    createdat: string;
    student: {
        username: string;
    };
}

// Interfața trebuie să conțină și ID-ul temei
interface Submission {
    submissionId: number;
    description: string;
    submittedAt: string;
    googleDriveFileId: string;
    student: {
        username: string;
    };
    assignment: {
        assid: number;
        asstitle: string;
    };
}

const ProfessorPage: React.FC = () => {
    const username = localStorage.getItem('username') || 'Profesor';
    const navigate = useNavigate();

    // State pentru Teme Create (Patrat_mijloc)
    const [assignments, setAssignments] = useState<Assignment[]>([]);
    const [loadingAssignments, setLoadingAssignments] = useState(true);
    const [errorAssignments, setErrorAssignments] = useState<string | null>(null);

    // State pentru Teme Trimise (Patrat_dreapta)
    const [submissions, setSubmissions] = useState<Submission[]>([]);
    const [loadingSubmissions, setLoadingSubmissions] = useState(true);
    const [errorSubmissions, setErrorSubmissions] = useState<string | null>(null);

    // Funcția pentru Teme Create
    const fetchAssignments = async () => {
        const token = localStorage.getItem('token');
        try {
            setLoadingAssignments(true);
            const response = await axios.get<Assignment[]>(
                'http://localhost:9090/api/assignments/professor/me',
                { headers: { Authorization: `Bearer ${token}` } }
            );
            setAssignments(response.data);
            setErrorAssignments(null);
        } catch (err) {
            console.error("Eroare la preluarea temelor:", err);
            setErrorAssignments("Nu am putut încărca temele.");
        } finally {
            setLoadingAssignments(false);
        }
    };

    // Funcția pentru Teme Trimise
    const fetchSubmissions = async () => {
        const token = localStorage.getItem('token');
        try {
            setLoadingSubmissions(true);
            const response = await axios.get<Submission[]>(
                'http://localhost:9090/api/assignments/submissions/professor/me',
                { headers: { Authorization: `Bearer ${token}` } }
            );
            setSubmissions(response.data);
            setErrorSubmissions(null);
        } catch (err) {
            setErrorSubmissions("Nu am putut încărca rezolvările studenților.");
        } finally {
            setLoadingSubmissions(false);
        }
    };


    // Acum se apelează ambele funcții la încărcarea paginii
    useEffect(() => {
        fetchAssignments();
        fetchSubmissions();
    }, []); // Se execută o singură dată

    // --- Funcția de randare pentru Patrat_dreapta ---
    const renderSubmissionsTable = () => {
        if (loadingSubmissions) return <p className="loading-message">Se încarcă rezolvările...</p>;
        if (errorSubmissions) return <p className="error-message">{errorSubmissions}</p>;
        if (submissions.length === 0) {
            return <p className="loading-message">Nicio temă nu a fost trimisă încă.</p>;
        }
        return (
            <table className="assignment-table-prof">

                <thead>
                <tr>
                    <th>Student</th>
                    <th>Titlu Temă</th>
                    <th>Data Trimiterii</th>
                    <th>Acțiuni</th>
                </tr>
                </thead>
                <tbody>
                {submissions.map((sub) => (
                    <tr key={sub.submissionId}>
                        <td>{sub.student.username}</td>
                        <td>{sub.assignment.asstitle}</td>
                        <td>{new Date(sub.submittedAt).toLocaleDateString('ro-RO')}</td>
                        <td className="actions-cell">
                            <button className="action-button grade-button" onClick={() => handleGrade(sub.assignment.assid)}>
                                Corectează
                            </button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        );
    };

    const handleCreateClick = () => {
        navigate('/create');
    };

    const handleDelete = async (id: number) => {
        if (window.confirm('Ești sigur că vrei să ștergi această temă?')) {
            const token = localStorage.getItem('token');
            try {
                await axios.delete(`http://localhost:9090/api/assignments/${id}`, {
                    headers: { Authorization: `Bearer ${token}` }
                });
                fetchAssignments(); // Re-încarcă lista de teme
            } catch (err) {
                console.error("Eroare la ștergerea temei:", err);
                alert("Eroare la ștergerea temei.");
            }
        }
    };

    // Funcție pentru navigare
    const handleGrade = (id: number) => {
        navigate(`/grade/${id}`); // Folosim backticks (`)
    };

    // Funcția de randare pentru Patrat_mijloc
    const renderAssignmentsTable = () => {
        if (loadingAssignments) return <p className="loading-message">Se încarcă temele...</p>;
        if (errorAssignments) return <p className="error-message">{errorAssignments}</p>;
        if (assignments.length === 0) {
            return <p className="loading-message">Nu ai creat nicio temă încă.</p>;
        }
        return (
            <table className="assignment-table-prof">

                <thead>
                <tr>
                    <th>Titlu</th>
                    <th>Student</th>
                    <th>Acțiuni</th>
                </tr>
                </thead>
                <tbody>
                {assignments.map((assignment) => (
                    <tr key={assignment.assid}>
                        <td>{assignment.asstitle}</td>
                        <td>{assignment.student.username}</td>
                        <td className="actions-cell">
                            {/* Aici daca daca este necesar de un buton de vew*/}
                            <button
                                className="action-button delete-button"
                                onClick={() => handleDelete(assignment.assid)}
                            >
                                Șterge
                            </button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        );
    };

    // Return JSX
    return (
        <div className="dashboard-wrapper">
            <VideoBackground/>
            <div className="Patrat_stanga">
                <div className="dashboard-card card-welcome">
                    <h2>Bine ai venit, {username}!</h2>
                    <p>Selectează o tema  pentru elevi:</p>
                    <button onClick={handleCreateClick} className="card-button">
                        Creează o Temă Nouă
                    </button>
                </div>
            </div>

            <div className="Patrat_mijloc">
                <h3>Teme Create de Tine</h3>
                {renderAssignmentsTable()}
            </div>

            <div className="Patrat_dreapta">
                <h3>Rezolvări Primite</h3>
                {renderSubmissionsTable()}
            </div>
        </div>
    );
};

export default ProfessorPage;