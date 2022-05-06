import { React, useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { ActorDetailCard } from '../components/ActorDetailCard';
import { ActorMovieListCard } from '../components/ActorMovieListCard';

import './ActorDetailPage.scss';

export const ActorDetailPage = () => {
    const [person, setPerson] = useState(null);

    const { actorId } = useParams();

    useEffect(
        () => {
            const fetchPerson = async () => {
                const response = await fetch(process.env.REACT_APP_BACKEND_URL + `person/${actorId}`);
                const personData = await response.json();
                setPerson(personData);
            };
            fetchPerson();

        }, [actorId]
    );

    if (!person) {
        return <h1>Searching for actor</h1>
    }

    return (
        <div className="ActorDetailPage">
            <ActorDetailCard key={person.id} actor={person} showActorDeails="true" />
            <ActorMovieListCard id={actorId} actor={person} />
        </div>
    );
}