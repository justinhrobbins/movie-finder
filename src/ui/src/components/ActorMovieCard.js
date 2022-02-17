import { React, useEffect, useState } from 'react';
import { MovieFlatrateProviderCard } from './MovieFlatrateProviderCard';

export const ActorMovieCard = ({ movie }) => {
    const [movieWithWatchProviders, setMovieWithWatchProviders] = useState({});

    useEffect(
        () => {
            const fetchWatchProviders = async () => {
                const response = await fetch(`http://localhost:8080/movie/${movie.id}/watchproviders`);
                const data = await response.json();
                setMovieWithWatchProviders(data);
            };

            fetchWatchProviders();
        }, []
    );

    let flatrateProviders;
    if (!movieWithWatchProviders["watch/providers"] || !movieWithWatchProviders["watch/providers"].results || !movieWithWatchProviders["watch/providers"].results.US || !movieWithWatchProviders["watch/providers"].results.US.flatrate) {
        flatrateProviders = [];
    } else {
        flatrateProviders = movieWithWatchProviders["watch/providers"].results.US.flatrate;
    }

    const baseUrl = "https://image.tmdb.org/t/p/" + "w45" + "/";
    const defaultMovieUrl = "https://www.themoviedb.org/assets/2/v4/glyphicons/basic/glyphicons-basic-4-user-grey-d8fe957375e70239d6abdd549fd7568c89281b2179b5f4470e2e12895792dfa5.svg"
    const movieUrl = !movie.poster_path || movie.poster_path === "" ? defaultMovieUrl : baseUrl + movie.poster_path;

    return (
        <div className="ActorMovieCard">
            <p>{movie.title} - {movie.media_type} - {movie.release_date}</p>
            <img src={movieUrl} width="45" />
            {
                flatrateProviders.length == 0
                    ? <p>Not currently on any streaming services</p>
                    : flatrateProviders.map(flatrateProvider => <MovieFlatrateProviderCard key={flatrateProvider.provider_id} flatrateProvider={flatrateProvider} />)
            }
            <hr />
        </div>
    );
}