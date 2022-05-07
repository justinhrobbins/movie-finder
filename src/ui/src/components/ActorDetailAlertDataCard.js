import { React, useContext, useEffect, useState } from 'react';
import { UserContext } from "../UserContext";
import { Link } from 'react-router-dom';

import './scss/ActorDetailAlertDataCard.scss';

export const ActorDetailAlertDataCard = ({ actor }) => {
    const { loggedInUser } = useContext(UserContext);
    const [actorMoveCounts, setActorMovieCounts] = useState(null);
    const [subscriptionsLink, setSubscriptionsLink] = useState(null);

    useEffect(
        () => {
            const fetchActorMovieCounts = async () => {
                const response = await fetch(process.env.REACT_APP_BACKEND_URL + `person/${actor.id}/movie/counts`, {
                    method: 'GET',
                    headers: {
                        "Content-Type": "application/json"
                    }
                });
                const actorMoveCounts = await response.json();
                setActorMovieCounts(actorMoveCounts);
            };

            fetchActorMovieCounts();
        }, []
    );

    useEffect(
        () => {
            if (!loggedInUser) {
                setSubscriptionsLink("Login to filter movies by your Subscriptions");
            } else if (!loggedInUser.streamingServices || !loggedInUser.streamingServices.length > 0) {
                setSubscriptionsLink("Configure your Subscriptions to filter by Subscriptions");
            } else {
                if (actorMoveCounts) {
                    let subscriptionCount = 0;
                    actorMoveCounts.subscriptions
                        .map(subscription => {
                            if (loggedInUser.streamingServices.includes(subscription.subcriptionService)) {
                                subscriptionCount = subscriptionCount + subscription.movieCount;
                            } else {
                                subscriptionCount = subscriptionCount + 0;
                            }
                        });
                    setSubscriptionsLink(<Link className="actor-detail-alert-data-card-link" to={`/actors/${actor.id}?sort=newest&filter=subscriptions`}>On your Subscriptions: {subscriptionCount}</Link>)
                }
            }
        }, [loggedInUser, actorMoveCounts]
    );

    if (!actorMoveCounts) {
        return <h3>Loading details for actor {actor.name}...</h3>
    }

    return (
        <div className="ActorDetailAlertDataCard">
            <div className="actor-detail-alert-data-card-label"><Link className="actor-detail-alert-data-card-link" to={`/actors/${actor.id}?sort=newest&filter=upcoming`}>Upcoming Movies: {actorMoveCounts.upcomingMovies}</Link></div>
            <div className="actor-detail-alert-data-card-label"><Link className="actor-detail-alert-data-card-link" to={`/actors/${actor.id}?sort=newest&filter=recent`}>Recent Movies: {actorMoveCounts.recentMovies}</Link></div>
            <div className="actor-detail-alert-data-card-label"><Link className="actor-detail-alert-data-card-link" to={`/actors/${actor.id}?sort=newest&filter=all`}>Total Movies: {actorMoveCounts.totalMovies}</Link></div>
            <div className="actor-detail-alert-data-card-label">{subscriptionsLink}</div>
        </div>
    );
}