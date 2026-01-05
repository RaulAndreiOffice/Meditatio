import React, { useState, useEffect } from "react";
import axios from "axios";
import "./CreateAssignment.css";

interface Student {
    user_id: number;
    username: string;
}

export default function CreateAssignment() {
    const [title, setTitle] = useState("");
    const [details, setDetails] = useState("");
    const [file, setFile] = useState<File | null>(null);
    const [preview, setPreview] = useState<string | null>(null);
    const [students, setStudents] = useState<Student[]>([]);
    const [selectedStudent, setSelectedStudent] = useState("");

    useEffect(() => {
        const token = localStorage.getItem("token"); // sau "jwtToken" - verifică ce cheie folosești
        axios
            .get<Student[]>("http://localhost:9090/api/users/students", {
                headers: { Authorization: `Bearer ${token}` },
            })
            .then((res) => setStudents(res.data))
            .catch((err) => console.error("Eroare la preluarea elevilor:", err));
    }, []);

    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0];
        if (!file) return;
        setFile(file);

        const reader = new FileReader();
        reader.onload = () => setPreview(reader.result as string);
        reader.readAsDataURL(file);
    };

    const handleSubmit = async (e: React.FormEvent) => {
        const token = localStorage.getItem("token");
        e.preventDefault();
        if (!title || !details || !selectedStudent) {
            alert("Completează toate câmpurile!");
            return;
        }

        const formData = new FormData();
        formData.append("asstitle", title);
        formData.append("description", details);
        formData.append("studentId", selectedStudent);

        if (file) formData.append("file", file);

        try {
            await axios.post("http://localhost:9090/api/assignments/create", formData, {
                headers: { "Content-Type": "multipart/form-data",
                            "Authorization": `Bearer ${token}`,
                },
            });
            alert("Tema a fost creată cu succes!");
            setTitle("");
            setDetails("");
            setFile(null);
            setPreview(null);
            setSelectedStudent("");
        } catch (err) {
            console.error("Eroare la creare temă:", err);
            alert("Eroare la creare temă!");
        }
    };

    return (
        <div className="create-assignment-container">
            <div className="image-preview">
                {preview ? (
                    <img src={preview} alt="Preview" />
                ) : (
                    <label className="upload-label">
                        <input type="file" accept="image/*" onChange={handleFileChange} hidden />
                        <span>Adaugă o imagine 📸</span>
                    </label>
                )}
            </div>

            <div className="assignment-form">
                <h2>Creează o temă nouă</h2>

                <input
                    type="text"
                    placeholder="Titlul temei"
                    value={title}
                    onChange={(e: React.ChangeEvent<HTMLInputElement>) => setTitle(e.target.value)}
                    className="input-field"
                />

                <textarea
                    placeholder="Detalii despre temă..."
                    value={details}
                    onChange={(e: React.ChangeEvent<HTMLTextAreaElement>) => setDetails(e.target.value)}
                    className="textarea-field"
                ></textarea>

                <select
                    value={selectedStudent}
                    onChange={(e: React.ChangeEvent<HTMLSelectElement>) => setSelectedStudent(e.target.value)}
                    className="select-field"
                >
                    <option value="">Selectează elevul</option>
                    {students.map((s) => (
                        <option key={s.user_id} value={s.user_id}>
                            {s.username}
                        </option>
                    ))}
                </select>

                <button className="create-btn" onClick={handleSubmit}>
                    Creează tema
                </button>
            </div>
        </div>
    );
}
