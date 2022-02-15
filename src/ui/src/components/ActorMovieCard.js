import { React } from 'react';
// import { Link } from 'react-router-dom';

export const ActorMovieCard = ({ movie }) => {
    if (!movie) return null;

    const baseUrl = "https://image.tmdb.org/t/p/" + "w45" + "/";
    const defaultMovieUrl = "https://www.themoviedb.org/assets/2/v4/glyphicons/basic/glyphicons-basic-4-user-grey-d8fe957375e70239d6abdd549fd7568c89281b2179b5f4470e2e12895792dfa5.svg"
    const movieUrl = !movie.poster_path || movie.poster_path === "" ? defaultMovieUrl : baseUrl + movie.poster_path;
    // const actorDetailRoute = `/actors/${actor.id}`;

    return (
        <div className="ActorMovieCard">
            {/* <p><Link to={actorDetailRoute}>{actor.name}</Link></p> */}
            <p>{movie.title}</p>
            <img src={movieUrl} width="45" />
        </div>
    );
}