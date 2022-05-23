import { React, useState, useEffect } from 'react';
import { ActorDetailCard } from './ActorDetailCard';

import './scss/ActorAlertDetailCard.scss';

export const ActorAlertDetailCard = ({ providedActor }) => {
    const [actor, setActor] = useState(providedActor);

    const removeActor = (removeActor) => setActor(removeActor);

    useEffect(
        () => {
            setActor(providedActor);
        }, [providedActor]
    );

    if (!actor) return null;

    return (
        <div className="ActorAlertDetailCard">
            <ActorDetailCard key={actor.id} providedActor={actor} removeActor={removeActor} showActorBio={false} />
        </div>
    );
}