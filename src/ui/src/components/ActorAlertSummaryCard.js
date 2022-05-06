import { React } from 'react';
import { Link } from 'react-router-dom';

import './ActorAlertSummaryCard.scss';

export const ActorAlertSummaryCard = ({ userActorAlerts}) => {

    return (
        <div className="ActorAlertSummaryCard">
            <div className="actor-alert-data-card-total-actors"><Link className="actor-detail-alert-data-card-link" to={`/actoralerts?sort=name&filter=all`} title="Show all your actors">You are following {userActorAlerts.actorCount} Actors</Link></div>
            <div className="actor-alert-data-card-upcoming-releases"><Link className="actor-detail-alert-data-card-link" to={`/actoralerts?sort=name&filter=upcoming`} title="Filter by upcoming releases">Your Actors with Upcoming Releases: {userActorAlerts.upcomingMovieCount}</Link></div>
            <div className="actor-alert-data-card-recent-releases"><Link className="actor-detail-alert-data-card-link" to={`/actoralerts?sort=name&filter=recent`} title="Filter by recent releases">Your Actors with Recent Releases: {userActorAlerts.recentMovieCount}</Link></div>
            <div className="actor-alert-data-card-recent-releases"><Link className="actor-detail-alert-data-card-link" to={`/actoralerts?sort=name&filter=subscriptions`} title="Filter by subscriptions">Your Actors with Movies on your Subscriptions: {userActorAlerts.subscriptionCount}</Link></div>
        </div>
    );
}