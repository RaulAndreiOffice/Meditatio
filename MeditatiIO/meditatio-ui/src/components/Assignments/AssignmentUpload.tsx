import React, { useState, ChangeEvent } from 'react';
import axios from 'axios';


interface AssignmentUploadProps {
    assignmentId: number;
}

const AssignmentUpload: React.FC<AssignmentUploadProps> = ({ assignmentId }) => {

    const [selectedFile, setSelectedFile] = useState<File | null>(null);
    const [isLoading, setIsLoading] = useState(false);
    const [message, setMessage] = useState<string>('');
    const [uploadedFileUrl, setUploadedFileUrl] = useState<string | null>(null);

    const handleFileChange = (event: ChangeEvent<HTMLInputElement>) => {
        if (event.target.files && event.target.files[0]) {
            setSelectedFile(event.target.files[0]);
            setMessage('');
            setUploadedFileUrl(null);
        }
    };

    const handleSubmit = async () => {
        if (!selectedFile) {
            setMessage('Te rog să selectezi un fișier.');
            return;
        }

        setIsLoading(true);
        setMessage('');
        const token = localStorage.getItem('token');
        const userId = localStorage.getItem('userId');

        if (!userId || !token) {
            setMessage('Eroare: Nu ești autentificat corespunzător.');
            setIsLoading(false);
            return;
        }

        const formData = new FormData();
        formData.append('file', selectedFile);
        formData.append('userId', userId);

        const url = `http://localhost:8080/api/assignments/${assignmentId}/upload`;

        try {
            const response = await axios.post(url, formData, {
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'multipart/form-data'
                }
            });

            const { fileUrl, message: successMessage } = response.data;

            setUploadedFileUrl(fileUrl);
            setMessage(successMessage);
            setSelectedFile(null);

        } catch (error: any) {
            console.error('Eroare la încărcarea temei:', error);
            const serverError = error.response?.data?.message || 'A apărut o eroare la încărcare.';
            setMessage(serverError);
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="upload-container">
            <div className="form-group-upload">
                <label>Încarcă rezolvarea:</label>
                <input
                    type="file"
                    onChange={handleFileChange}
                    accept="image/*,.pdf,.doc,.docx"
                />
            </div>

            <button
                onClick={handleSubmit}
                disabled={!selectedFile || isLoading}
                className="upload-button"
            >
                {isLoading ? 'Se încarcă...' : 'Trimite Tema'}
            </button>

            {message && (
                <p className={`upload-message ${uploadedFileUrl ? 'success' : 'error'}`}>
                    {message}
                </p>
            )}

            {uploadedFileUrl && (
                <div className="file-preview">
                    <p>Fișierul tău trimis (previzualizare):</p>
                    <embed
                        src={uploadedFileUrl}
                        width="100%"
                        height="300px"
                    />
                </div>
            )}
        </div>
    );
};

export default AssignmentUpload;