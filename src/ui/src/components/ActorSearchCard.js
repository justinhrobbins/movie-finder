import { React } from 'react';
import { Link } from 'react-router-dom';

export const ActorSearchCard = ({ actor }) => {
    if (!actor) return null;

    const baseUrl = "https://image.tmdb.org/t/p/" + "w45" + "/";
    const defaultActorUrl = "https://www.themoviedb.org/assets/2/v4/glyphicons/basic/glyphicons-basic-4-user-grey-d8fe957375e70239d6abdd549fd7568c89281b2179b5f4470e2e12895792dfa5.svg"
    const actorUrl = actor.profile_path === "" ? defaultActorUrl : baseUrl + actor.profile_path;
    const actorDetailRoute = `/actors/${actor.id}`;

    return (
        <div className="ActorSearchCard">
            <p><Link to={actorDetailRoute}>{actor.name}</Link></p>
            <img src={actorUrl} width="45" />
        </div>
    );
}