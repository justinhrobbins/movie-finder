import { React, useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { ActorDetailCard } from '../components/ActorDetailCard';
import { ActorMovieListCard } from '../components/ActorMovieListCard';

import './scss/ActorDetailPage.scss';

export const ActorDetailPage = () => {
    const [actor, setActor] = useState(null);

    const { actorId } = useParams();

    useEffect(
        () => {
            const fetchPerson = async () => {
                const response = await fetch(process.env.REACT_APP_BACKEND_URL + `actors/${actorId}`);
                const actorData = await response.json();
                setActor(actorData);
            };
            fetchPerson();

        }, [actorId]
    );

    if (!actor) {
        return <h1>Searching for actor</h1>
    }

    return (
        <div className="ActorDetailPage">
            <ActorDetailCard key={actor.actorId} actor={actor} showActorBio={true} />
            <ActorMovieListCard id={actorId} actor={actor.person} />
        </div>
    );
}