import { React, useContext, useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { ActorDetailCard } from '../components/ActorDetailCard';
import { ActorMovieListCard } from '../components/ActorMovieListCard';
import { UserContext } from "../UserContext";

import './ActorDetailPage.scss';

export const ActorDetailPage = () => {
    const { loggedInUser, setLoggedInUserContext } = useContext(UserContext);
    const [person, setPerson] = useState(null);
    const [actorDetails, setActortDetails] = useState(null);
    const [isActorAlertActive, setIsAlertActiveforActort] = useState(false);
    const { actorId } = useParams();

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

            if (loggedInUser) {
                const fetchActorAlert = async () => {
                    const response = await fetch(`http://localhost:8080/actoralerts/${actorId}`, {
                        method: 'GET',
                        headers: {
                            "Content-Type": "application/json",
                            'Authorization': `Bearer ${loggedInUser.tokenId}`
                        }
                    })
                        .then(response => {
                            if (response.status == 200) {
                                setIsAlertActiveforActort(true);
                            } else {
                                setIsAlertActiveforActort(false);
                            }
                        });
                };
                fetchActorAlert();
            }
        }, []
    );

    if (!person) {
        return <h1>Searching for actor</h1>
    }

    if (!actorDetails) {
        return <h1>Loading page for actor {person.name}</h1>
    }

    return (
        <div className="ActorDetailPage">
            <ActorDetailCard key={person.id} actor={person} actorDetails={actorDetails} isAlertActiveForActor={isActorAlertActive} />
            <ActorMovieListCard id={actorId} actor={person} />
        </div>
    );
}