import { React, useContext, useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { UserContext } from "../UserContext";
import { ActorDetails } from '../components/ActorDetails';
import { ActorMovieList } from '../components/ActorMovieList';

import './scss/ActorDetailPage.scss';

export const ActorDetailPage = () => {
    const { loggedInUser } = useContext(UserContext);
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

        }, [actorId, loggedInUser]
    );

    if (!actor) {
        return <h1>Searching for actor</h1>
    }

    return (
        <div className="ActorDetailPage">
            <ActorDetails key={actor.actorId} providedActor={actor} showActorBio={true} />
            <ActorMovieList id={actorId} actor={actor.person} />
        </div>
    );
}