import { React, useContext, useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { UserContext } from "../UserContext";
import { ActorDetailAlertDataCard } from './ActorDetailAlertDataCard';
import { ActorBioCard } from './ActorBioCard';

import './scss/ActorDetailCard.scss';

export const ActorDetailCard = ({ actor, showActorBio, removeActor }) => {
    const { loggedInUser } = useContext(UserContext);
    const [isActorAlertActive, setIsActorAlertActive] = useState(false);

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
                <div className="actor-detail-card-content-actor-name">
                    <Link className="actor-detail-card-content-actor-name-link" to={actorDetailRoute}>{actor.name}</Link>
                </div>
                <div className="actor-detail-card-content-alert-data">
                    <ActorDetailAlertDataCard key={actor.id} actor={actor} />
                </div>
                {showActorBio == true &&
                    <ActorBioCard key={actor.id} actor={actor} />
                }
            </div>
        </div>
    );
}