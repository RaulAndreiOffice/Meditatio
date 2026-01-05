import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import axios from 'axios';
import './StudentAssignmentView.css';


interface Assignment {
    assid: number;
    asstitle: string;
    description: string;
    createdat: string;
    fileUrl: string;
}

const StudentAssignmentView: React.FC = () => {
    const { id } = useParams<{ id: string }>();
    const [assignment, setAssignment] = useState<Assignment | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [file, setFile] = useState<File | null>(null);
    const [description, setDescription] = useState("");
    const [isUploading, setIsUploading] = useState(false);

    useEffect(() => {
        const fetchAssignment = async () => {
            const token = localStorage.getItem('token');
            try {
                const response = await axios.get<Assignment>(
                    `http://localhost:9090/api/assignments/${id}`,
                    {
                        headers: { Authorization: `Bearer ${token}` }
                    }
                );
                setAssignment(response.data);
            } catch (err) {
                console.error("Eroare la preluarea temei:", err);
                setError("Nu am putut încărca detaliile temei.");
            }
        };

        fetchAssignment();
    }, [id]); // Se re-apelează dacă ID-ul din URL se schimbă
    const handleSubmit = async () => {
        if (!file) {
            alert("Te rog să încarci un fișier.");
            return;
        }
        if (!id) return;

        setIsUploading(true);
        const token = localStorage.getItem('token');
        const formData = new FormData();
        formData.append("file", file);
        formData.append("description", description);
        formData.append("assignmentId", id);

        try {
            await axios.post("http://localhost:9090/api/assignments/submit", formData, {
                headers: {
                    "Content-Type": "multipart/form-data",
                    "Authorization": `Bearer ${token}`,
                },
            });
            alert("Tema a fost trimisă cu succes!");
            // Resetăm și închidem modalul
            setIsModalOpen(false);
            setFile(null);
            setDescription("");
        } catch (err) {
            console.error("Eroare la trimiterea temei:", err);
            alert("Eroare la trimiterea temei!");
        } finally {
            setIsUploading(false);
        }
    };

    if (error) {
        return <div className="loading-error">{error}</div>;
    }

    if (!assignment) {
        return <div className="loading-error">Se încarcă tema...</div>;
    }

    return (
        <div className="assignment-view-container">
            <div className="image-pane">
                {assignment.fileUrl ? (
                    <img src={assignment.fileUrl} alt="Imaginea temei" />
                ) : (
                    <p>Această temă nu are o imagine atașată.</p>
                )}
            </div>

            <div className="details-pane">
                <h2>{assignment.asstitle}</h2>
                <p className="assignment-date">
                    Primită la: {new Date(assignment.createdat).toLocaleString('ro-RO')}
                </p>

                <h3>Descriere:</h3>
                <p className="assignment-description">
                    {assignment.description}
                </p>

                {/* Aici poți adăuga formularul (dacă nu vrei popup)
                    sau butonul de popup */}
            </div>

            {/*  Butonul verde (popup) --- */}
            <button className="submit-popup-button" onClick={() => setIsModalOpen(true)}>
                Trimite Rezolvarea
            </button>

            {/* Modalul (Popup-ul) --- */}
            {isModalOpen && (
                <div className="modal-overlay">
                    <div className="modal-content">
                        <h3>Încarcă Rezolvarea</h3>

                        <div className="form-group-modal">
                            <label>Fișierul tău (PDF, JPG, PNG)</label>
                            <input
                                type="file"
                                onChange={(e) => setFile(e.target.files ? e.target.files[0] : null)}
                            />
                        </div>

                        <div className="form-group-modal">
                            <label>Descriere (opțional)</label>
                            <textarea
                                value={description}
                                onChange={(e) => setDescription(e.target.value)}
                                placeholder="Adaugă un comentariu pentru profesor..."
                            />
                        </div>

                        <div className="modal-actions">
                            <button
                                className="modal-button-send"
                                onClick={handleSubmit}
                                disabled={isUploading}
                            >
                                {isUploading ? "Se trimite..." : "Trimite"}
                            </button>
                            <button
                                className="modal-button-cancel"
                                onClick={() => setIsModalOpen(false)}
                            >
                                Anulează
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

export default StudentAssignmentView;