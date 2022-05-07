import { React, useContext, useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { UserContext } from "../UserContext";
import { ActorDetailAlertDataCard } from './ActorDetailAlertDataCard';

import './scss/ActorDetailCard.scss';

export const ActorDetailCard = ({ actor, showActorDeails, removeActor }) => {
    const { loggedInUser } = useContext(UserContext);
    const [isActorAlertActive, setIsActorAlertActive] = useState(false);
    const [showFullBio, setShowFullBio] = useState(false);

    const createActorAlertText = "Follow Actor";
    const removeActorAlertText = "Unfollow Actor";

    useEffect(
        () => {
            if (loggedInUser) {
                const fetchActorAlert = async () => {
                    const response = await fetch(process.env.REACT_APP_BACKEND_URL + `actoralerts/${actor.id}`, {
                        method: 'GET',
                        headers: {
                            "Content-Type": "application/json",
                            'Authorization': `Bearer ${loggedInUser.tokenId}`
                        }
                    })
                        .then(response => response.text())
                        .then(isAlertActiveForActor => {
                            if (isAlertActiveForActor == 'true') {
                                setIsActorAlertActive(true);
                            } else {
                                setIsActorAlertActive(false);
                            }
                        });
                };
                fetchActorAlert();
            }
        }, []
    );

    if (!actor) return null;

    const baseUrl = "https://image.tmdb.org/t/p/w185/";
    const defaultActorPhotoUrl = "https://www.themoviedb.org/assets/2/v4/glyphicons/basic/glyphicons-basic-4-user-grey-d8fe957375e70239d6abdd549fd7568c89281b2179b5f4470e2e12895792dfa5.svg"
    const actorPhotoUrl = (!actor || !actor.profile_path || actor.profile_path === "" || actor.profile_path === null)
        ? defaultActorPhotoUrl : baseUrl + actor.profile_path;

    const actorDetailRoute = `/actors/${actor.id}`;

    const manageActorAlert = (actorId) => {
        const actorAlert = { actorId: actorId };

        if (!loggedInUser) {
            alert('Login to save Actor Alerts');
            return;
        }

        if (isActorAlertActive === false) {
            fetch(process.env.REACT_APP_BACKEND_URL + 'actoralerts', {
                method: 'POST',
                headers: {
                    "Content-Type": "application/json",
                    'Authorization': `Bearer ${loggedInUser.tokenId}`
                },
                body: JSON.stringify(actorAlert)
            }).then(response => {
                if (response.status == 200) {
                    setIsActorAlertActive(true)
                }
            })
        } else {
            fetch(process.env.REACT_APP_BACKEND_URL + `actoralerts/${actorId}/`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${loggedInUser.tokenId}`
                }
            }).then(response => {
                if (response.status == 204) {
                    setIsActorAlertActive(false)

                    if (removeActor) {
                        removeActor(null);
                    }
                }
            })
        };
    };

    const toggleBio = () => {
        setShowFullBio(!showFullBio)
    }

    return (
        <div className="ActorDetailCard">

            <div className="actor-detail-card-image-container">
                <img className="actor-detail-card-image-image" src={actorPhotoUrl} alt={actor.name} title={actor.name} />
                <div className="actor-detail-card-image-details">
                    <button className="actor-detail-card-image-button" value={actor.id} onClick={(e) => { manageActorAlert(e.target.value) }}>
                        {isActorAlertActive === true ? removeActorAlertText : createActorAlertText}
                    </button>
                </div>
            </div>

            <div className="actor-detail-card-content">
                <div className="actor-detail-card-content-actor-bio-container">
                    <div className="actor-detail-card-content-actor-name"><Link className="actor-detail-card-content-actor-name-link" to={actorDetailRoute}>{actor.name}</Link>
                    </div>
                    {showActorDeails == "true" &&
                        <div className="actor-detail-card-content-container">
                            <span className="actor-detail-card-content-label">Birthday:</span> {actor.birthday}
                        </div>
                    }
                    {(showActorDeails == "true" && actor && actor.deathday && actor.deathday.length > 0)
                        ? <div className="actor-detail-card-content-container">
                            <span className="actor-detail-card-content-label">Day of Death:</span> {actor.deathday}
                        </div>
                        : <div className="actor-detail-card-content-container">
                            <span className="actor-detail-card-content-label"></span>
                        </div>
                    }
                    {showActorDeails == "true" &&
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
                    }
                </div>
                <div className="actor-detail-card-content-alert-data">
                    <ActorDetailAlertDataCard key={actor.id} actor={actor} />
                </div>
            </div>
        </div>
    );
}