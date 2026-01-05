import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Link, useNavigate } from 'react-router-dom';

import './App.css';


import Login from './components/Auth/Login';
import Register from './components/Auth/Register';
import ProfilePage from './components/Profile/ProfilePage';
import Home from './components/Home/Home';
import Chatbot from "./components/Chatbot/Chatbot";
import StudentPage from './components/Profile/StudentPage';
import ProfessorPage from "./components/Profile/ProfessorPage";
import CreateAssignment from "./components/Assignments/CreateAssignment";
import AssignmentDetails from "./components/Assignments/AssignmentDetails";
import StudentAssignmentView from "./components/Assignments/StudentAssignmentView";
import GradeAssignmentPage from "./components/Assignments/GradeAssignmentPage";
import GradedAssignmentView from "./components/Assignments/GradedAssignmentView";


const LogoutButton: React.FC<{ onLogout: () => void }> = ({ onLogout }) => {
    const navigate = useNavigate();

    const handleClick = () => {
        onLogout();
        navigate('/login'); // Redirecționează la login după logout
    };

    return (
        <button onClick={handleClick} className="nav-button logout-button">
            Logout
        </button>
    );
};
const App: React.FC = () => {
    const [currentUser, setCurrentUser] = useState<string | null>(localStorage.getItem('username'));
    const [currentRole, setCurrentRole] = useState<string | null>(localStorage.getItem('role'));


    const handleLogin = (username: string, role: string) => {
        setCurrentUser(username);
        setCurrentRole(role);
    };

    const handleLogout = () => {
        setCurrentUser(null);
        setCurrentRole(null);
        localStorage.removeItem('token');
        localStorage.removeItem('username');
        localStorage.removeItem('userId');
        localStorage.removeItem('role');
    };
    const getRoleDisplayName = (role: string | null) => {
        if (role === 'ROLE_STUDENT') return 'Student';
        if (role === 'ROLE_PROFESOR') return 'Profesor';
        return 'Utilizator';
    };

    return (
        <Router>
            <div>
                {/* ----- MODIFICAT: Bara de navigare ----- */}
                <nav className="nav-container">
                    <div>
                        {/* Link către "Acasă" - în funcție de rol */}
                        <Link to={currentRole === 'ROLE_PROFESOR' ? '/professor' : (currentRole === 'ROLE_STUDENT' ? '/student' : '/')} className="nav-button">
                            Acasă
                        </Link>
                    </div>

                    {currentUser && currentRole ? (
                        <div style={{ display: 'flex', alignItems: 'center' }}>
                            {/* Afișarea "Rol: Nume" */}
                            <span className="welcome-user">
                                {getRoleDisplayName(currentRole)}: {currentUser}
                            </span>

                            <Link to="/profile" className="nav-button">
                                Profil
                            </Link>

                            <LogoutButton onLogout={handleLogout} />
                        </div>
                    ) : (
                        <div>
                            {/* Utilizatorul nu e logat */}
                            <Link to="/login" className="nav-button">Login</Link>
                            <Link to="/register" className="nav-button">Register</Link>
                        </div>
                    )}
                </nav>
                {/* ------------------------------------------ */}

                {/* Chatbot-ul va fi afișat doar dacă ești logat */}
                {currentUser && <Chatbot />}

                <div className="content-container">
                    <Routes>
                        <Route path="/" element={<Home />} />
                        <Route path="/login" element={<Login onLoginSuccess={handleLogin} />} />
                        <Route path="/register" element={<Register />} />

                        {/* Rutele pentru roluri  */}
                        <Route path="/student" element={<StudentPage />} />
                        <Route path="/professor" element={<ProfessorPage />} />

                        <Route path="/" element={<ProfessorPage />} />
                        <Route path="/create" element={<CreateAssignment />} />

                        <Route path="/profile" element={<ProfilePage />} />

                        <Route path="/student" element={<StudentPage />} />
                        <Route path="/assignment/:id" element={<AssignmentDetails />} />

                        <Route path="/assignment-view/:id" element={<StudentAssignmentView />} />

                        <Route path="/grade/:id" element={<GradeAssignmentPage />} />

                        <Route path="/graded-view/:id" element={<GradedAssignmentView />} />


                    </Routes>
                </div>
            </div>
        </Router>
    );
};


export default App;