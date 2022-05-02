import { React, useEffect, useState } from 'react';
import { useParams, useLocation } from 'react-router-dom';
import { ActorDetailCard } from '../components/ActorDetailCard';
import { ActorMovieListCard } from '../components/ActorMovieListCard';

import './ActorDetailPage.scss';

export const ActorDetailPage = () => {
    const [person, setPerson] = useState(null);
    const [actorDetails, setActortDetails] = useState(null);
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

            const fetchActorAlertDetails = async () => {
                const response = await fetch(`http://localhost:8080/person/${actorId}/details`, {
                    method: 'GET',
                    headers: {
                        "Content-Type": "application/json"
                    }
                });
                const alertDetails = await response.json();
                setActortDetails(alertDetails);
            };

            fetchActorAlertDetails();
        }, [location]
    );

    if (!person) {
        return <h1>Searching for actor</h1>
    }

    if (!actorDetails) {
        return <h2>Loading page for actor {person.name}...</h2>
    }

    return (
        <div className="ActorDetailPage">
            <ActorDetailCard key={person.id} actor={person} actorDetails={actorDetails} />
            <ActorMovieListCard id={actorId} actor={person} />
        </div>
    );
}