import { React, useContext, useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { UserContext } from "../UserContext";
import { ActorMovieMetrics } from './ActorMovieMetrics';
import { ActorBio } from './ActorBio';

import './scss/ActorDetails.scss';

export const ActorDetails = ({ providedActor, showActorBio, removeActor }) => {
    const { loggedInUser } = useContext(UserContext);
    const [actor, setActor] = useState(providedActor);
    const [isUserFollowingActor, setIsUserFollowingActor] = useState(false);

    const createActorAlertText = "Follow Actor";
    const removeActorAlertText = "Unfollow Actor";

    useEffect(
        () => {
            setActor(providedActor);
        }, [providedActor]
    );

    useEffect(
        () => {
            if (loggedInUser) {
                const fetchIsUserFollowingActor = async () => {
                    const response = await fetch(process.env.REACT_APP_BACKEND_URL + `actoralerts/${actor.actorId}`, {
                        method: 'GET',
                        headers: {
                            "Content-Type": "application/json",
                            'Authorization': `Bearer ${loggedInUser.tokenId}`
                        }
                    })
                        .then(response => response.text())
                        .then(isAlertActiveForActor => {
                            if (isAlertActiveForActor == 'true') {
                                setIsUserFollowingActor(true);
                            } else {
                                setIsUserFollowingActor(false);
                            }
                        });
                };
                fetchIsUserFollowingActor();
            }
        }, []
    );

    if (!actor) return null;
    
    const baseUrl = "https://image.tmdb.org/t/p/w185/";
    const defaultActorPhotoUrl = "https://www.themoviedb.org/assets/2/v4/glyphicons/basic/glyphicons-basic-4-user-grey-d8fe957375e70239d6abdd549fd7568c89281b2179b5f4470e2e12895792dfa5.svg"
    const actorPhotoUrl = (!actor || !actor.person.profile_path || actor.person.profile_path === "" || actor.person.profile_path === null)
        ? defaultActorPhotoUrl : baseUrl + actor.person.profile_path;

    const actorDetailRoute = `/actors/${actor.actorId}`;

    const toggleActorAlert = (actorId) => {
        const actorAlert = { actorId: actorId };

        if (!loggedInUser) {
            alert('Login to save Actor Alerts');
            return;
        }

        if (isUserFollowingActor === false) {
            fetch(process.env.REACT_APP_BACKEND_URL + 'actoralerts', {
                method: 'POST',
                headers: {
                    "Content-Type": "application/json",
                    'Authorization': `Bearer ${loggedInUser.tokenId}`
                },
                body: JSON.stringify(actorAlert)
            }).then(response => {
                if (response.status == 200) {
                    setIsUserFollowingActor(true)
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
                    setIsUserFollowingActor(false)

                    if (removeActor) {
                        removeActor(actorId);
                    }
                }
            })
        };
    };

    return (
        <div className="ActorDetails">
            <div className="actor-detail-image-container">
                <img className="actor-detail-image-image" src={actorPhotoUrl} alt={actor.person.name} title={actor.person.name} />
                <div className="actor-detail-image-details">
                    <button className="actor-detail-image-button" value={actor.actorId} onClick={(e) => { toggleActorAlert(e.target.value) }}>
                        {isUserFollowingActor === true ? removeActorAlertText : createActorAlertText}
                    </button>
                </div>
            </div>

            <div className="actor-detail-content">
                <div className="actor-detail-content-actor-name">
                    <Link className="actor-detail-content-actor-name-link" to={actorDetailRoute}>{actor.person.name}</Link>
                </div>
                <div className="actor-detail-content-alert-data">
                    <ActorMovieMetrics key={actor.actorId} providedActor={actor} />
                </div>
                {showActorBio == true &&
                    <ActorBio key={actor.actorId} actor={actor.person} />
                }
            </div>
        </div>
    );
}