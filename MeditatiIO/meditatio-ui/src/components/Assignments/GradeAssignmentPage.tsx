import React, { useState, useEffect, useRef } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import CanvasDraw from 'react-canvas-draw';
import './GradeAssignmentPage.css';


interface Assignment {
    assid: number;
    asstitle: string;
    fileUrl: string;
}

// Interfață pentru rezolvarea trimisă (a studentului)
interface Submission {
    submissionId: number;
    googleDriveFileId: string;
    originalFileName: string;
}

const GradeAssignmentPage: React.FC = () => {
    const { id } = useParams<{ id: string }>(); // ID-ul temei (Assignment ID)
    const token = localStorage.getItem('token');
    const navigate = useNavigate();

    // State pentru cele două imagini
    const [originalAssignment, setOriginalAssignment] = useState<Assignment | null>(null);
    const [studentSubmission, setStudentSubmission] = useState<Submission | null>(null);

    const [error, setError] = useState<string | null>(null);
    const [loading, setLoading] = useState(true);

    const [grade, setGrade] = useState<string>(""); // Folosim string pentru input
    const [isSubmitting, setIsSubmitting] = useState(false);

    // Funcție pentru a trimite nota
    const handleSubmitGrade = async () => {
        if (!studentSubmission) {
            alert("Nu există o rezolvare de notat.");
            return;
        }
        const numericGrade = parseInt(grade);
        if (isNaN(numericGrade) || numericGrade < 1 || numericGrade > 10) {
            alert("Te rog să introduci o notă validă (între 1 și 10).");
            return;
        }
        const drawingData = canvasRef.current?.getSaveData();
        if (!drawingData) {
            alert("Eroare la salvarea desenului.");
            return;
        }

        setIsSubmitting(true);
        setError(null);

        const gradeData = {
            grade: numericGrade,
            feedbackDrawing: drawingData
        };
        try {
            await axios.post(
                // Folosim ID-ul REZOLVĂRII (submissionId)
                `http://localhost:9090/api/assignments/submission/${studentSubmission.submissionId}/grade`,
                gradeData,
                { headers: { Authorization: `Bearer ${token}` } }
            );

            alert("Nota a fost trimisă cu succes!");
            navigate('/professor'); // Redirecționare înapoi la pagina de profesor

        } catch (err: any) {
            console.error("Eroare la trimiterea notei:", err);
            setError(err.response?.data?.message || "Eroare la trimiterea notei.");
        } finally {
            setIsSubmitting(false);
        }
    };


    // Referință pentru canvas-ul de desen
    const canvasRef = useRef<CanvasDraw>(null);

    useEffect(() => {
        const fetchAllData = async () => {
            try {
                setLoading(true);
                // 1. Preluăm detaliile temei originale (imaginea profesorului)
                const assignmentRes = await axios.get<Assignment>(
                    `http://localhost:9090/api/assignments/${id}`,
                    { headers: { Authorization: `Bearer ${token}` } }
                );
                setOriginalAssignment(assignmentRes.data);

                // 2. Preluăm rezolvarea studentului (imaginea studentului)
                try {
                    const submissionRes = await axios.get<Submission>(
                        `http://localhost:9090/api/assignments/submission/${id}`,
                        { headers: { Authorization: `Bearer ${token}` } }
                    );
                    setStudentSubmission(submissionRes.data);
                } catch (subError) {
                    setError("Studentul nu a trimis încă o rezolvare pentru această temă.");
                }

            } catch (err) {
                console.error("Eroare la preluarea datelor:", err);
                setError("Nu am putut încărca datele temei.");
            } finally {
                setLoading(false);
            }
        };

        fetchAllData();
    }, [id, token]);

    //  Funcții pentru a controla canvas-ul
    const clearCanvas = () => {
        canvasRef.current?.clear();
    };

    const undoCanvas = () => {
        canvasRef.current?.undo();
    };

    //  Afișare
    if (loading) {
        return <div className="grade-loading">Se încarcă...</div>;
    }

    if (error && !studentSubmission) {
        return <div className="grade-loading error">{error}</div>;
    }

    if (!originalAssignment) {
        return <div className="grade-loading error">Tema nu a fost găsită.</div>;
    }

    return (
        <div className="grade-page-container">
            {/* --- PANOU STÂNGA (Canvas de desen) --- */}
            <div className="grade-panel-left">
                <h3>Rezolvarea Studentului (Desenează aici)</h3>

                {/* Butoanele de control */}
                <div className="canvas-controls">
                    <button onClick={clearCanvas}>Șterge Tot</button>
                    <button onClick={undoCanvas}>Anulează</button>
                    {/* TODO: Adaugă butoane pentru a schimba culoarea, ex. roșu */}
                </div>

                {studentSubmission ? (
                    <CanvasDraw
                        ref={canvasRef}
                        lazyRadius={0}
                        brushRadius={2}
                        brushColor="#FF0000" // Culoare roșie (pentru corectat)
                        catenaryColor="#AAAAAA"
                        // Încărcăm imaginea studentului ca fundal
                        imgSrc={studentSubmission.googleDriveFileId}
                        canvasWidth={800} // Lățime canvas
                        canvasHeight={600} // Înălțime canvas
                        style={{ border: '1px solid #000' }}
                    />
                ) : (
                    <div className="grade-loading error">Studentul nu a trimis tema.</div>
                )}
            </div>

            {/* PANOU DREAPTA (Referință)  */}
            <div className="grade-panel-right">
                <h3>Tema Originală (Referință)</h3>
                <h4>{originalAssignment.asstitle}</h4>
                {originalAssignment.fileUrl ? (
                    <img src={originalAssignment.fileUrl} alt="Tema originală" />
                ) : (
                    <p>Tema nu a avut o imagine atașată.</p>
                )}
                {studentSubmission && (
                    <div className="grading-section">
                        <hr />
                        <h3>Acordă o notă</h3>
                        <label htmlFor="grade-input">Notă (1-10):</label>
                        <input
                            id="grade-input"
                            type="number"
                            min="1"
                            max="10"
                            value={grade}
                            onChange={(e) => setGrade(e.target.value)}
                            className="grade-input-field" // Adaugă stiluri pentru asta în CSS
                        />
                        <button
                            onClick={handleSubmitGrade}
                            disabled={isSubmitting}
                            className="submit-grade-button" // Adaugă stiluri pentru asta în CSS
                        >
                            {isSubmitting ? "Se trimite..." : "Trimite Nota"}
                        </button>

                        {/* Afișează eroarea de notare */}
                        {error && !loading && (
                            <p style={{ color: 'red', marginTop: '10px' }}>{error}</p>
                        )}
                    </div>
                )}
            </div>

        </div>
    );
};

export default GradeAssignmentPage;