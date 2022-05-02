import { React, useEffect, useState } from 'react';
import { useParams, useLocation } from 'react-router-dom';
import { ActorDetailCard } from '../components/ActorDetailCard';
import { ActorMovieListCard } from '../components/ActorMovieListCard';

import './ActorDetailPage.scss';

export const ActorDetailPage = () => {
    const [person, setPerson] = useState(null);

    const { actorId } = useParams();
    const location = useLocation();

    useEffect(
        () => {
            const fetchPerson = async () => {
                const response = await fetch(`http://localhost:8080/person/${actorId}`);
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
            <ActorDetailCard key={person.id} actor={person} />
            <ActorMovieListCard id={actorId} actor={person} />
        </div>
    );
}