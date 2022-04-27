import { React } from 'react';
import { Link } from 'react-router-dom';

import './ActortAlertSummaryCard.scss';

export const ActortAlertSummaryCard = ({ userActorAlerts}) => {

    return (
        <div className="ActortAlertSummaryCard">
            <div className="actor-alert-data-card-total-actors">You have {userActorAlerts.actorAlertCount} Actor Alerts configured</div>
            <div className="actor-alert-data-card-upcoming-releases"><Link className="actor-detail-alert-data-card-link" to={`/actoralerts?sort=name&filter=upcoming`} title="Filter by upcoming releases">Your Actors with Upcoming Releases: {userActorAlerts.upcomingMovieCount}</Link></div>
            <div className="actor-alert-data-card-recent-releases"><Link className="actor-detail-alert-data-card-link" to={`/actoralerts?sort=name&filter=recent`} title="Filter by recent releases">Your Actors with Recent Releases: {userActorAlerts.recentMovieCount}</Link></div>
        </div>
    );
}