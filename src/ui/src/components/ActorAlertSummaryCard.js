import { React } from 'react';
import { Link } from 'react-router-dom';

import './scss/ActorAlertSummaryCard.scss';

export const ActorAlertSummaryCard = ({ actors }) => {

    return (
        <div className="ActorAlertSummaryCard">
            <div className="actor-alert-data-card-movie-summary">
            <div className="actor-alert-data-card-recent-releases">Total movies from your actors: {actors.movieCounts.totalMovies}</div>
                <div className="actor-alert-data-card-recent-releases"><Link className="actor-detail-alert-data-card-link" to={`/mymovies?sort=name&filter=upcoming`} title="">Upcoming movies from your actors: {actors.movieCounts.upcomingMovies}</Link></div>
                <div className="actor-alert-data-card-recent-releases"><Link className="actor-detail-alert-data-card-link" to={`/mymovies?sort=name&filter=recent`} title="">Recent releases from your actors: {actors.movieCounts.recentMovies}</Link></div>
                <div className="actor-alert-data-card-recent-releases"><Link className="actor-detail-alert-data-card-link" to={`/mymovies?sort=name&filter=subscriptions`} title="">Movies from your actors on your subscriptions: {actors.movieCounts.moviesOnSubscriptions}</Link></div>
            </div>
            <div className="actor-alert-data-card-actor-summary">
                <div className="actor-alert-data-card-total-actors"><Link className="actor-detail-alert-data-card-link" to={`/actoralerts?sort=name&filter=all`} title="Show all your actors">Actors followed: {actors.actorCounts.actorCount}</Link></div>
                <div className="actor-alert-data-card-upcoming-releases"><Link className="actor-detail-alert-data-card-link" to={`/actoralerts?sort=name&filter=upcoming`} title="Filter by upcoming releases">Actors with upcoming releases: {actors.actorCounts.upcomingMovieCount}</Link></div>
                <div className="actor-alert-data-card-recent-releases"><Link className="actor-detail-alert-data-card-link" to={`/actoralerts?sort=name&filter=recent`} title="Filter by recent releases">Actors with recent releases: {actors.actorCounts.recentMovieCount}</Link></div>
                <div className="actor-alert-data-card-recent-releases"><Link className="actor-detail-alert-data-card-link" to={`/actoralerts?sort=name&filter=subscriptions`} title="Filter by subscriptions">Actors with movies on your subscriptions: {actors.actorCounts.subscriptionCount}</Link></div>
            </div>
        </div>
    );
}