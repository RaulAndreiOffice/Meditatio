import  React, { useState } from 'react';
import axios from 'axios';
import './Chatbot.css';


interface Message {
    sender: 'user' | 'bot';
    text: string;
}

const Chatbot: React.FC = () => {
    const [isOpen, setIsOpen] = useState(false); // Starea ferestrei (deschisă/închisă)
    const [messages, setMessages] = useState<Message[]>([]); // Istoricul mesajelor
    const [input, setInput] = useState(''); // Textul din input
    const [isLoading, setIsLoading] = useState(false);

    const toggleChat = () => setIsOpen(!isOpen);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!input.trim()) return;

        const token = localStorage.getItem("token");
        if (!token) {
            setMessages(prev => [...prev, { sender: 'bot', text: 'Te rog autentifică-te mai întâi.' }]);
            return; // evităm "Bearer null"
        }

        setMessages(prev => [...prev, { sender: 'user', text: input }]);
        setInput('');
        setIsLoading(true);

        try {
            const response = await axios.post(
                'http://localhost:9090/api/chat',
                {
                    username: localStorage.getItem("username") || "Anonim",
                    message: input
                },
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                }
            );

            setMessages(prev => [...prev, { sender: 'bot', text: response.data }]);
        } catch (error: any) {
            console.error('Eroare la chat:', error?.response || error);
            const serverMsg = error?.response?.data?.message || 'Oops! Ceva nu a mers bine.';
            setMessages(prev => [...prev, { sender: 'bot', text: serverMsg }]);
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="chatbot-container">
            {/* Fereastra de chat */}
            {isOpen && (
                <div className="chat-window">
                    <div className="chat-header">
                        Asistent AI
                        <button onClick={toggleChat} className="chat-close-btn">&times;</button>
                    </div>
                    <div className="chat-body">
                        {messages.map((msg, index) => (
                            <div key={index} className={`chat-message ${msg.sender}`}>
                                {msg.text}
                            </div>
                        ))}
                        {isLoading && <div className="chat-message bot loading">...</div>}
                    </div>
                    <form className="chat-footer" onSubmit={handleSubmit}>
                        <input
                            type="text"
                            value={input}
                            onChange={(e) => setInput(e.target.value)}
                            placeholder="Scrie un mesaj..."
                        />
                        <button type="submit">Send</button>
                    </form>
                </div>
            )}

            {/* Butonul plutitor */}
            <button onClick={toggleChat} className="chat-toggle-button">
                {isOpen ? 'X' : '🤖'}
            </button>
        </div>
    );
};

export default Chatbot;