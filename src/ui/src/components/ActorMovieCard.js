import { React, useContext, useEffect, useState } from 'react';
import { UserContext } from "../UserContext";
import { MovieFlatrateProviderCard } from './MovieFlatrateProviderCard';

import './ActorMovieCard.scss';

export const ActorMovieCard = ({ providedMovie, filterBySubscriptions }) => {
    const { loggedInUser } = useContext(UserContext);
    const [movie, setMovie] = useState(providedMovie);
    const [flatrateProviders, setFlatrateProviders] = useState(null);
    const [movieUrl, setMovieUrl] = useState(null);
    const [flatrateProviderLabel, setFlatrateProviderLabel] = useState(null);

    useEffect(
        () => {
            let isMounted = true;
            const fetchWatchProviders = async () => {
                const response = await fetch(`http://localhost:8080/movie/${providedMovie.id}/watchproviders`);
                const movieWithWatchProviders = await response.json();

                let flatrateProviders;
                if (!movieWithWatchProviders
                    || !movieWithWatchProviders["watch/providers"]
                    || !movieWithWatchProviders["watch/providers"].results
                    || !movieWithWatchProviders["watch/providers"].results.US
                    || !movieWithWatchProviders["watch/providers"].results.US.flatrate) {
                    flatrateProviders = [];
                } else {
                    flatrateProviders = movieWithWatchProviders["watch/providers"].results.US.flatrate;
                }

                if (isMounted) setFlatrateProviders(flatrateProviders);
            };

            fetchWatchProviders();

            return () => {
                isMounted = false
            };
        }, []
    );

    useEffect(
        () => {
            const baseUrl = "https://image.tmdb.org/t/p/" + "w185" + "/";
            const defaultMovieUrl = "https://www.themoviedb.org/assets/2/v4/glyphicons/basic/glyphicons-basic-4-user-grey-d8fe957375e70239d6abdd549fd7568c89281b2179b5f4470e2e12895792dfa5.svg"
            !movie || !movie.poster_path || movie.poster_path === "" || movie.poster_path === null
                ? setMovieUrl(defaultMovieUrl)
                : setMovieUrl(baseUrl + movie.poster_path);
        }, [movie]
    );

    useEffect(
        () => {
            if (!flatrateProviders || flatrateProviders.length == 0) {
                setFlatrateProviderLabel(<span>Not currently on any streaming services</span>);
            } else {
                setFlatrateProviderLabel(<span>Available on these streaming services:</span>);
            }
            if (filterBySubscriptions == true && flatrateProviders) {
                if (!loggedInUser
                    || !loggedInUser.streamingServices 
                    || !flatrateProviders.some(e => loggedInUser.streamingServices.includes(e.provider_name))) {
                    setMovie(null);
                }
            }
        }, [flatrateProviders]
    );

    if (!movie) return null;
    if (!flatrateProviders) return null;

    return (
        <div className="ActorMovieCard">
            <div className="actor-movie-card-image">
                <img width="185" src={movieUrl} />
            </div>
            <div className="actor-movie-card-info">
                <div className="actor-movie-card-info-header">
                    <span className="actor-movie-card-info-title">{movie.title}</span>
                </div>
                <div className="actor-movie-card-info-content">RELEASE DATE: {movie.release_date} ({movie.original_language})</div>
                <div className="actor-movie-card-info-content">LANGUAGE: {movie.original_language}</div>
                <div className="actor-movie-card-info-content">ROLE: {movie.character}</div>
                <div className="actor-movie-card-info-content">Popularity: {movie.popularity}</div>
                <div className="actor-movie-card-info-content">OVERVIEW: {movie.overview}</div>
            </div>
            <div className="actor-movie-card-providers-section">
                <div className="actor-movie-card-providers-section-label">{flatrateProviderLabel}</div>
                <div className="actor-movie-card-providers-section-content">
                    {
                        flatrateProviders.map(flatrateProvider => <MovieFlatrateProviderCard key={flatrateProvider.provider_id} flatrateProvider={flatrateProvider} />)
                    }
                </div>
            </div>
        </div>
    );
}