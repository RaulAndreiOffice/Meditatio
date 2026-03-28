import React from 'react';
import backgroundImage from '../../assets/Backgraund_Medi.png';
import './VideoBackground.css';

const VideoBackground: React.FC = () => {
    return (
        <div className="video-background" style={{ backgroundImage: `url(${backgroundImage})` }} />
    );
};

export default VideoBackground;