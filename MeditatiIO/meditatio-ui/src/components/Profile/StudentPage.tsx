import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import VideoBackground from "../VideoBackground/VideoBackground";
// CSS: Importăm stilurile pentru layout-ul cu 3 pătrate
import './ProfessorPage.css';
// CSS: Importăm STILURILE PENTRU TABEL (de pe fundal întunecat)
import './ProfilePage.css'; // <-- ACESTA ESTE FIȘIERUL CHEIE
// CSS: Importăm stilul pentru cursor (opțional, dar util)
import "./StudentPage.css";
import GradeAssignmentPage from "../Assignments/GradeAssignmentPage";
import GradedAssignmentView from "../Assignments/GradedAssignmentView";



interface Assignment {
    assid: number;
    asstitle: string;
    description: string;
    createdat: string; // Data va veni ca string
    fileUrl: string;
}
interface GradedSubmission {
    submissionId: number;
    submittedAt: string; // Data trimiterii
    grade: number;
    assignment: {
        assid: number;
        asstitle: string;
    };
}

export default function StudentPage() {
    const [assignments, setAssignments] = useState<Assignment[]>([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    const [gradedSubs, setGradedSubs] = useState<GradedSubmission[]>([]);
    const [isLoadingGrades, setIsLoadingGrades] = useState(true);
    const [errorGrades, setErrorGrades] = useState<string | null>(null);

    const navigate = useNavigate();



    useEffect(() => {
        const token = localStorage.getItem("token");

        axios.get<Assignment[]>('http://localhost:9090/api/assignments/student/me', {
            headers: { Authorization: `Bearer ${token}` }
        })
            .then(response => {
                setAssignments(response.data);
                setIsLoading(false);
            })
            .catch(err => {
                console.error("Eroare la preluarea temelor:", err);
                setError("Nu am putut încărca temele. Te rog, încearcă mai târziu.");
                setIsLoading(false);
            });
        axios.get<GradedSubmission[]>('http://localhost:9090/api/assignments/submissions/student/graded', {
            headers: { Authorization: `Bearer ${token}` }
        })
            .then(response => {
                setGradedSubs(response.data);
            })
            .catch(err => {
                console.error("Eroare la preluarea notelor:", err);
                setErrorGrades("Nu am putut încărca notele.");
            })
            .finally(() => {
                setIsLoadingGrades(false);
            });

    }, []);
//  Funcție de randare pentru Pătratul Mijloc (Teme Corectate) ---
    const renderGradesContent = () => {
        if (isLoadingGrades) {
            return <p className="loading-message">Se încarcă notele...</p>;
        }
        if (errorGrades) {
            return <p className="error-message">{errorGrades}</p>;
        }
        if (gradedSubs.length === 0) {
            return <p className="loading-message">Nu ai nicio temă corectată încă.</p>;
        }
        return (
            <table className="assignment-table-prof">
                <thead>
                <tr>
                    <th>Titlu Tema</th>
                    <th>Data Trimiterii</th>
                    <th>Notă</th>
                </tr>
                </thead>
                <tbody>
                {gradedSubs.map((sub) => (
                    // Poți adăuga un onClick aici dacă vrei să duci studentul
                    // la o pagină unde vede corectura (necesită modificări suplimentare)

                    // ===== MODIFICAT AICI =====
                    <tr
                        key={sub.submissionId}
                        onClick={() => handleGradedClick(sub.submissionId)}
                        className="clickable-row" // Folosim aceeași clasă ca la teme
                    >
                        {/* ========================= */}

                        <td>{sub.assignment.asstitle}</td>
                        <td>{new Date(sub.submittedAt).toLocaleDateString('ro-RO')}</td>
                        <td style={{ fontWeight: 'bold', color: '#4CAF50', fontSize: '1.1em' }}>
                            {sub.grade}
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        );
    };



    const handleAssignmentClick = (id: number) => {
        navigate(`/assignment-view/${id}`);
    };

    // ===== FUNCȚIE NOUĂ ADĂUGATĂ AICI =====
    const handleGradedClick = (id: number) => {
        navigate(`/graded-view/${id}`);
    };
    // =====================================

    const renderContent = () => {
        if (isLoading) {
            // Folosim clasa din ProfilePage.css (text alb)
            return <p className="loading-message">Se încarcă temele...</p>;
        }

        if (error) {
            // Folosim clasa din ProfilePage.css (text roșu)
            return <p className="error-message">{error}</p>;
        }

        if (assignments.length === 0) {
            // Folosim clasa din ProfilePage.css (text alb)
            return <p className="loading-message">Nu ai primit nicio temă încă.</p>;
        }

        return (
            //Folosim 'assignment-table-prof' (din ProfilePage.css)
            <table className="assignment-table-prof">
                <thead>
                <tr>
                    <th>Titlu Tema</th>
                    <th>Data Primirii</th>
                </tr>
                </thead>
                <tbody>
                {assignments.map((tema) => (
                    <tr
                        key={tema.assid}
                        onClick={() => handleAssignmentClick(tema.assid)}
                        // Folosim 'clickable-row' (din StudentPage.css) pentru cursor
                        className="clickable-row"
                    >
                        <td>{tema.asstitle}</td>
                        <td>{new Date(tema.createdat).toLocaleDateString('ro-RO')}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        );
    };


    return (
        <div className="professor-page-container">
            <VideoBackground />


            <div className="Patrat_stanga">
                <h3>Temele Primite</h3>
                {renderContent()}
            </div>

            <div className="Patrat_mijloc">
                <h3>Temele Corectate</h3>
                {renderGradesContent()} {/* NOUA funcție */}

            </div>

            <div className="Patrat_dreapta">

            </div>
        </div>
    );
}