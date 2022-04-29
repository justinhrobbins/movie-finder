import { React, useContext, useEffect, useState } from 'react';
import { UserContext } from "../UserContext";
import { Link } from 'react-router-dom';

import './ActorDetailAlertDataCard.scss';

export const ActorDetailAlertDataCard = ({ actor, actorDetails }) => {
    const { loggedInUser, setLoggedInUserContext } = useContext(UserContext);
    const [subscriptionsLink, setSubscriptionsLink] = useState(null);

    useEffect(
        () => {
            if (!loggedInUser) {
                setSubscriptionsLink("Login to filter movies by your Subscriptions");
            } else if (!loggedInUser.streamingServices || !loggedInUser.streamingServices.length > 0) {
                setSubscriptionsLink("Configure your Subscriptions to filter by Subscriptions");
            } else {
                let subscriptionCount = 0;
                actorDetails.subscriptions
                    .map(subscription => {
                        if (loggedInUser.streamingServices.includes(subscription.subcriptionService)) {
                            subscriptionCount = subscriptionCount + subscription.movieCount;
                        } else {
                            subscriptionCount = subscriptionCount + 0;
                        }
                    });
                setSubscriptionsLink(<Link className="actor-detail-alert-data-card-link" to={`/actors/${actor.id}?sort=newest&filter=subscriptions`}>Movies on your Subscriptions: {subscriptionCount}</Link>)
            }
        }, []
    );

    return (
        <div className="ActorDetailAlertDataCard">
            <div className="actor-detail-alert-data-card-label"><Link className="actor-detail-alert-data-card-link" to={`/actors/${actor.id}?sort=newest&filter=upcoming`}>Upcoming Movie Releases: {actorDetails.upcomingMovies}</Link></div>
            <div className="actor-detail-alert-data-card-label"><Link className="actor-detail-alert-data-card-link" to={`/actors/${actor.id}?sort=newest&filter=recent`}>Recent Movie Releases: {actorDetails.recentMovies}</Link></div>
            <div className="actor-detail-alert-data-card-label"><Link className="actor-detail-alert-data-card-link" to={`/actors/${actor.id}?sort=newest&filter=all`}>Total Movies: {actorDetails.totalMovies}</Link></div>
            <div className="actor-detail-alert-data-card-label">{subscriptionsLink}</div>
        </div>
    );
}