import { React, useContext, useEffect, useState } from 'react';
import { UserContext } from "../UserContext";
import { Link } from 'react-router-dom';

import './scss/ActorMovieMetrics.scss';

export const ActorMovieMetrics = ({ providedActor }) => {
    const { loggedInUser } = useContext(UserContext);
    const [actor, setActor] = useState(providedActor);
    const [actorMoveCounts, setActorMovieCounts] = useState({});
    const [subscriptionsLink, setSubscriptionsLink] = useState(null);

    useEffect(
        () => {
            setActor(providedActor);

        }, [providedActor]
    );

    useEffect(
        () => {
            const fetchActorMovieCounts = async () => {
                var headers = new Headers();
                headers.append("Content-Type", "application/json");

                if (loggedInUser) {
                    headers.append("Authorization", `Bearer ${loggedInUser.tokenId}`);
                }

                const requestOptions = {
                    method: 'GET',
                    headers: headers
                };

                const response = await fetch(process.env.REACT_APP_BACKEND_URL + `actors/${actor.actorId}/movies/counts`,
                    requestOptions);
                const actorMoveCounts = await response.json();
                setActorMovieCounts(actorMoveCounts);
            };

            if (!actor.movieCounts) {
                fetchActorMovieCounts();
            } else {
                setActorMovieCounts(actor.movieCounts);
            }

        }, [actor, loggedInUser]
    );

    useEffect(
        () => {
            if (!loggedInUser) {
                setSubscriptionsLink("Login to filter movies by your Subscriptions");
            } else if (!loggedInUser.streamingServices || !loggedInUser.streamingServices.length > 0) {
                setSubscriptionsLink("Configure your Subscriptions to filter by Subscriptions");
            } else {
                if (actorMoveCounts) {
                    if (actor.person.name === "Scarlett Johansson") {
                    }
                    setSubscriptionsLink(<Link className="actor-movie-metrics-link" to={`/actors/${actor.actorId}?sort=newest&filter=subscriptions`}>On your Subscriptions: {actorMoveCounts.moviesOnSubscriptions}</Link>)
                }
            }
        }, [loggedInUser, actorMoveCounts]
    );

    if (!actor) return null;

    if (!actorMoveCounts) {
        return <h3>Loading details for actor {actor.person.name}...</h3>
    }

    return (
        <div className="ActorMovieMetrics">
            <div className="actor-movie-metrics-label"><Link className="actor-movie-metrics-link" to={`/actors/${actor.actorId}?sort=newest&filter=upcoming`}>Upcoming Movies: {actorMoveCounts.upcomingMovies}</Link></div>
            <div className="actor-movie-metrics-label"><Link className="actor-movie-metrics-link" to={`/actors/${actor.actorId}?sort=newest&filter=recent`}>Recent Movies: {actorMoveCounts.recentMovies}</Link></div>
            <div className="actor-movie-metrics-label"><Link className="actor-movie-metrics-link" to={`/actors/${actor.actorId}?sort=newest&filter=all`}>Total Movies: {actorMoveCounts.totalMovies}</Link></div>
            <div className="actor-movie-metrics-label">{subscriptionsLink}</div>
        </div>
    );
}