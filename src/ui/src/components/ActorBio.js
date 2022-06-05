import { React, useState } from 'react';

import './scss/ActorBio.scss';

export const ActorBio = ({ actor }) => {
    const [showFullBio, setShowFullBio] = useState(false);

    const toggleBio = () => {
        setShowFullBio(!showFullBio)
    }

    if (!actor) return null;

    return (
        <div className="ActorBio">
            <div className="actor-bio-content-container">
                <span className="actor-bio-content-label">Birthday:</span> {actor.birthday}
            </div>
            {(actor && actor.deathday && actor.deathday.length > 0)
                ? <div className="actor-bio-content-container">
                    <span className="actor-bio-content-label">Day of Death:</span> {actor.deathday}
                </div>
                : <div className="actor-bio-content-container">
                    <span className="actor-bio-content-label"></span>
                </div>
            }
            <div>
                <div className="actor-bio-content-container">
                    <span className="actor-bio-content-label">Place of birth:</span> {actor.place_of_birth}
                </div>
                <div className="actor-bio-content-container">
                    <span className="actor-bio-content-label">Biography:</span><br />
                    {actor.biography && actor.biography.length > 1000 && showFullBio == true &&
                        <span className="actor-bio-bio">{actor.biography} <span className="actor-bio-link" onClick={toggleBio}>Show less</span></span>
                    }
                    {actor.biography && actor.biography.length > 1000 && showFullBio == false &&
                        <span className="actor-bio-bio">{actor.biography.substring(0, 1000)}... <span className="actor-bio-link" onClick={toggleBio}>Show more</span></span>
                    }
                    {actor.biography && actor.biography.length < 1001 &&
                        <span className="actor-bio-bio">{actor.biography}</span>
                    }
                </div>
            </div>
        </div>
    );
}