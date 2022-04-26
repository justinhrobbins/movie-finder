import { React, useEffect, useState } from 'react';
import { Link } from 'react-router-dom';

import './ActorDetailAlertDataCard.scss';

export const ActorDetailAlertDataCard = ({ actor, actorDetails}) => {
    const actorDetailRoute = `/actors/${actor.id}/`;

    return (
        <div className="ActorDetailAlertDataCard">
            <div className="actor-detail-alert-data-card-upcoming-releases"><Link className="actor-detail-alert-data-card-link" to={`/actors/${actor.id}?sort=newest&filter=upcoming`}>Upcoming Releases: {actorDetails.upcomingMovies}</Link></div>
            <div className="actor-detail-alert-data-card-recent-releases"><Link className="actor-detail-alert-data-card-link" to={`/actors/${actor.id}?sort=newest&filter=recent`}>Recent Releases: {actorDetails.recentMovies}</Link></div>
            <div className="actor-detail-alert-data-card-total-releases"><Link className="actor-detail-alert-data-card-link" to={`/actors/${actor.id}?sort=newest&filter=all`}>Total Releases: {actorDetails.totalMovies}</Link></div>
        </div>
    );
}