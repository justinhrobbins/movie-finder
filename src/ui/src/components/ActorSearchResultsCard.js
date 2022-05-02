import { React } from 'react';
import { Link } from 'react-router-dom';

import './ActorSearchResultsCard.scss';

export const ActorSearchResultsCard = ({ actor, clearSearchResults }) => {
    if (!actor) return null;

    const baseUrl = "https://image.tmdb.org/t/p/w45/";
    const defaultActorUrl = "https://www.themoviedb.org/assets/2/v4/glyphicons/basic/glyphicons-basic-4-user-grey-d8fe957375e70239d6abdd549fd7568c89281b2179b5f4470e2e12895792dfa5.svg"
    const actorUrl = actor.profile_path === "" ? defaultActorUrl : baseUrl + actor.profile_path;
    const actorDetailRoute = `/actors/${actor.id}`;

    return (
        <div className="ActorSearchResultsCard" onClick={clearSearchResults}>
            <Link to={actorDetailRoute}>
                <img className="actor-search-results-card-image" src={actorUrl} width="45" alt="" />
            </Link>
            <span className="actor-search-results-card-name"><Link to={actorDetailRoute}>{actor.name}</Link></span>
        </div>
    );
}