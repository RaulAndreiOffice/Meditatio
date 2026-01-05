import React,{useEffect, useRef} from 'react';
import backgroundVideo from '../../assets/backgroundVideo.mp4'; // Importă videoul
import './VideoBackground.css'; // Vom crea acest fișier CSS imediat

const VideoBackground: React.FC = () => {
    const videoRef = useRef<HTMLVideoElement>(null);

    useEffect(() => {
        if (videoRef.current) {
            videoRef.current.playbackRate = 0.6; // 0.5 = de 2x mai lent
        }
    }, []);
    return (
        <div className="video-background">
            <video ref={videoRef} autoPlay loop muted>
                <source src={backgroundVideo} type="video/mp4" />
                Browser-ul tău nu suportă tag-ul video.
            </video>
        </div>
    );
};

export default VideoBackground;