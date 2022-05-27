import { React } from 'react';
import { Link, useSearchParams } from 'react-router-dom';

import './scss/ActorSummaryCard.scss';

export const ActorSummaryCard = ({ actorCounts, movieCounts }) => {
    const [searchParams] = useSearchParams();
    const sortParam = searchParams.get('sort') ? searchParams.get('sort') : '';

    return (
        <div className="ActorSummaryCard">
            <div className="actor-summary-card-actor-summary">
                <div className="actor-summary-card-total-actors"><Link className="actor-detail-link" to={`/myactors?sort=${sortParam}&filter=all`} title="Show all your actors">Actors followed: {actorCounts.actorCount}</Link></div>
                <div className="actor-summary-card-upcoming-releases"><Link className="actor-detail-link" to={`/myactors?sort=${sortParam}&filter=upcoming`} title="Filter by upcoming releases">Actors with upcoming releases: {actorCounts.upcomingMovieCount}</Link></div>
                <div className="actor-summary-card-recent-releases"><Link className="actor-detail-link" to={`/myactors?sort=${sortParam}&filter=recent`} title="Filter by recent releases">Actors with recent releases: {actorCounts.recentMovieCount}</Link></div>
                <div className="actor-summary-card-recent-releases"><Link className="actor-detail-link" to={`/myactors?sort=${sortParam}&filter=subscriptions`} title="Filter by subscriptions">Actors with movies on your subscriptions: {actorCounts.subscriptionCount}</Link></div>
            </div>
            <div className="actor-summary-card-movie-summary">
                <div className="actor-summary-card-recent-releases">Total movies from your actors: {movieCounts.totalMovies}</div>
                <div className="actor-summary-card-recent-releases"><Link className="actor-detail-link" to={`/mymovies?sort=name&filter=upcoming`} title="">Upcoming movies from your actors: {movieCounts.upcomingMovies}</Link></div>
                <div className="actor-summary-card-recent-releases"><Link className="actor-detail-link" to={`/mymovies?sort=name&filter=recent`} title="">Recent releases from your actors: {movieCounts.recentMovies}</Link></div>
                <div className="actor-summary-card-recent-releases"><Link className="actor-detail-link" to={`/mymovies?sort=name&filter=subscriptions`} title="">Movies from your actors on your subscriptions: {movieCounts.moviesOnSubscriptions}</Link></div>
            </div>
        </div>
    );
}