import React from 'react';
import { useNavigate} from "react-router-dom";
import VideoBackground from "../VideoBackground/VideoBackground";
import "./Home.css";
import LogoImg from "../../assets/Home.jpg";


export default function Home(){
    const  navigate = useNavigate();
    return (

        <div className="home-page">

            <VideoBackground />

            <div className="med-card">
                <h1 className="med-title">Bine ai venit pe <span className="med-accent">Meditatio</span></h1>
                <p className="med-sub">Platforma ta de învățare online.</p>
            </div>

        </div>
    );
}