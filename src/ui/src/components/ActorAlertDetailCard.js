import { React, useEffect, useState } from 'react';
import { ActorDetailCard } from './ActorDetailCard';

import './ActorMovieCard.scss';

export const ActorAlertDetailCard = ({ actorId }) => {
    const [actor, setActor] = useState(null);

    const removeActor = actor => setActor(actor);

    useEffect(
        () => {
            const fetchActor = async () => {
                const response = await fetch(`http://localhost:8080/person/${actorId}`);
                const actorData = await response.json();
                setActor(actorData);
            };

            fetchActor();
        }, []
    );

    if (!actor) return null;

    return (
        <div className="ActorAlertDetailCard">
            <ActorDetailCard key={actor.id} actor={actor} isAlertActiveForActor="true" removeActor={removeActor} />
        </div>
    );
}