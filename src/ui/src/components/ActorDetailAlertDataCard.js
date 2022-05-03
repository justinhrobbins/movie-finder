import { React, useContext, useEffect, useState } from 'react';
import { UserContext } from "../UserContext";
import { Link } from 'react-router-dom';

import './ActorDetailAlertDataCard.scss';

export const ActorDetailAlertDataCard = ({ actor }) => {
    const { loggedInUser } = useContext(UserContext);
    const [actorDetails, setActortDetails] = useState(null);
    const [subscriptionsLink, setSubscriptionsLink] = useState(null);

    useEffect(
        () => {
            const fetchActorAlertDetails = async () => {
                const response = await fetch(`http://localhost:8080/person/${actor.id}/details`, {
                    method: 'GET',
                    headers: {
                        "Content-Type": "application/json"
                    }
                });
                const alertDetails = await response.json();
                setActortDetails(alertDetails);
            };

            fetchActorAlertDetails();
        }, []
    );

    useEffect(
        () => {
            if (!loggedInUser) {
                setSubscriptionsLink("Login to filter movies by your Subscriptions");
            } else if (!loggedInUser.streamingServices || !loggedInUser.streamingServices.length > 0) {
                setSubscriptionsLink("Configure your Subscriptions to filter by Subscriptions");
            } else {
                if (actorDetails) {
                    let subscriptionCount = 0;
                    actorDetails.subscriptions
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
        }, [loggedInUser, actorDetails]
    );

    if (!actorDetails) {
        return <h3>Loading details for actor {actor.name}...</h3>
    }

    return (
        <div className="ActorDetailAlertDataCard">
            <div className="actor-detail-alert-data-card-label"><Link className="actor-detail-alert-data-card-link" to={`/actors/${actor.id}?sort=newest&filter=upcoming`}>Upcoming Movies: {actorDetails.upcomingMovies}</Link></div>
            <div className="actor-detail-alert-data-card-label"><Link className="actor-detail-alert-data-card-link" to={`/actors/${actor.id}?sort=newest&filter=recent`}>Recent Movies: {actorDetails.recentMovies}</Link></div>
            <div className="actor-detail-alert-data-card-label"><Link className="actor-detail-alert-data-card-link" to={`/actors/${actor.id}?sort=newest&filter=all`}>Total Movies: {actorDetails.totalMovies}</Link></div>
            <div className="actor-detail-alert-data-card-label">{subscriptionsLink}</div>
        </div>
    );
}