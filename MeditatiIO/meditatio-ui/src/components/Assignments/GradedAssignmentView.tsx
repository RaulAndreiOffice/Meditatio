import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import CanvasDraw from 'react-canvas-draw';

import './GradeAssignmentPage.css';

// Interfața pentru datele primite
interface GradedViewData {
    assignmentTitle: string;
    grade: number;
    studentSubmissionUrl: string;
    professorDrawingData: string;
}

const GradedAssignmentView: React.FC = () => {
    const { id } = useParams<{ id: string }>(); // Acesta este submissionId
    const token = localStorage.getItem('token');

    const [submissionData, setSubmissionData] = useState<GradedViewData | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const fetchGradedData = async () => {
            try {
                const response = await axios.get<GradedViewData>(
                    `http://localhost:9090/api/assignments/submission/graded-view/${id}`,
                    { headers: { Authorization: `Bearer ${token}` } }
                );
                setSubmissionData(response.data);
            } catch (err: any) {
                console.error("Eroare la preluarea corecturii:", err);
                setError(err.response?.data?.message || "Nu am putut încărca corectura.");
            } finally {
                setLoading(false);
            }
        };

        fetchGradedData();
    }, [id, token]);

    if (loading) {
        return <div className="grade-loading">Se încarcă corectura...</div>;
    }

    if (error) {
        return <div className="grade-loading error">{error}</div>;
    }

    if (!submissionData) {
        return <div className="grade-loading error">Datele nu au putut fi încărcate.</div>;
    }

    return (
        // Reutilizăm containerul din GradeAssignmentPage
        <div className="grade-page-container" style={{ justifyContent: 'center' }}>

            {/* Afișăm doar un singur panou, centrat */}
            <div className="grade-panel-left">
                <h3>{submissionData.assignmentTitle}</h3>
                <h4 style={{ color: '#4CAF50' }}>Nota ta: {submissionData.grade}</h4>

                <CanvasDraw
                    disabled // STUDENTUL NU POATE DESENA
                    hideGrid // Ascundem grid-ul
                    lazyRadius={0}
                    // Încărcăm imaginea studentului ca fundal
                    imgSrc={submissionData.studentSubmissionUrl}
                    // Încărcăm desenul profesorului peste imagine
                    saveData={submissionData.professorDrawingData}
                    canvasWidth={800} // Asigură-te că e aceeași mărime
                    canvasHeight={600} // ca în pagina profesorului
                    style={{ border: '1px solid #000', margin: 'auto' }}
                />
            </div>

        </div>
    );
};

export default GradedAssignmentView;