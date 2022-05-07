import { React, useState } from 'react';

import './scss/ActorBioCard.scss';

export const ActorBioCard = ({ actor }) => {
    const [showFullBio, setShowFullBio] = useState(false);

    const toggleBio = () => {
        setShowFullBio(!showFullBio)
    }

    if (!actor) return null;

    return (
        <div className="actor-detail-card-content-actor-bio-container">
            <div className="actor-detail-card-content-container">
                <span className="actor-detail-card-content-label">Birthday:</span> {actor.birthday}
            </div>
            {(actor && actor.deathday && actor.deathday.length > 0)
                ? <div className="actor-detail-card-content-container">
                    <span className="actor-detail-card-content-label">Day of Death:</span> {actor.deathday}
                </div>
                : <div className="actor-detail-card-content-container">
                    <span className="actor-detail-card-content-label"></span>
                </div>
            }
            <div>
                <div className="actor-detail-card-content-container">
                    <span className="actor-detail-card-content-label">Place of birth:</span> {actor.place_of_birth}
                </div>
                <div className="actor-detail-card-content-container">
                    <span className="actor-detail-card-content-label">Biography:</span><br />
                    {actor.biography && actor.biography.length > 1000 && showFullBio == true &&
                        <span>{actor.biography} <span className="actor-detail-card-link" onClick={toggleBio}>Show less</span></span>
                    }
                    {actor.biography && actor.biography.length > 1000 && showFullBio == false &&
                        <span>{actor.biography.substring(0, 1000)}... <span className="actor-detail-card-link" onClick={toggleBio}>Show more</span></span>
                    }
                    {actor.biography && actor.biography.length < 1001 &&
                        <span>{actor.biography}</span>
                    }

                </div>
            </div>
        </div>
    );
}