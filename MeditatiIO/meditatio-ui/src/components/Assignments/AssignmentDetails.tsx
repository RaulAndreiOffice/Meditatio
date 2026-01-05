import React, { useEffect, useState } from "react";
import axios from "axios";
import { useParams } from "react-router-dom";
import "./AssignmentDetails.css";

interface Assignment {
    id: number;
    asstitle: string;
    description: string;
    fileUrl?: string;
    postedBy: string;
}

export default function AssignmentDetails() {
    const { id } = useParams<{ id: string }>();
    const [assignment, setAssignment] = useState<Assignment | null>(null);
    const [file, setFile] = useState<File | null>(null);

    useEffect(() => {
        const token = localStorage.getItem("token");
        axios
            .get<Assignment>(`http://localhost:9090/api/assignments/${id}`, {
                headers: { Authorization: `Bearer ${token}` },
            })
            .then((res) => setAssignment(res.data))
            .catch((err) => console.error("Eroare la încărcarea temei:", err));
    }, [id]);

    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const selected = e.target.files?.[0];
        if (selected) setFile(selected);
    };

    const handleSubmit = async () => {
        if (!file) {
            alert("Încarcă un fișier înainte!");
            return;
        }
        const token = localStorage.getItem("token");
        const formData = new FormData();
        formData.append("file", file);
        formData.append("assignmentId", id!);

        try {
            await axios.post("http://localhost:9090/api/assignments/submit", formData, {
                headers: {
                    "Content-Type": "multipart/form-data",
                    Authorization: `Bearer ${token}`,
                },
            });
            alert("Tema a fost trimisă profesorului!");
            setFile(null);
        } catch (err) {
            console.error("Eroare la trimiterea temei:", err);
            alert("Eroare la trimiterea temei!");
        }
    };

    if (!assignment) return <p>Se încarcă tema...</p>;

    return (
        <div className="assignment-details">
            <div className="assignment-left">
                <h2>{assignment.asstitle}</h2>
                <p>{assignment.description}</p>
                {assignment.fileUrl && (
                    <a href={assignment.fileUrl} target="_blank" rel="noreferrer">
                        Deschide fișierul original
                    </a>
                )}
            </div>

            <div className="assignment-right">
                <h3>Încarcă rezolvarea</h3>
                <input type="file" onChange={handleFileChange} />
                <button onClick={handleSubmit}>Trimite tema</button>
            </div>
        </div>
    );
}
