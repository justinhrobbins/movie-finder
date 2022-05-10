import { React, useState } from 'react';
import { ActorDetailCard } from './ActorDetailCard';

import './scss/ActorAlertDetailCard.scss';

export const ActorAlertDetailCard = ({ providedActor, actorDetails }) => {
    const [actor, setActor] = useState(providedActor);

    const removeActor = actor => setActor(actor);

    if (!actor) return null;

    return (
        <div className="ActorAlertDetailCard">
            <ActorDetailCard key={actor.id} actor={actor} actorDetails={actorDetails} removeActor={removeActor} showActorBio={false} />
        </div>
    );
}