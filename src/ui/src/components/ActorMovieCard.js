import { React, useEffect, useState } from 'react';
import { MovieFlatrateProviderCard } from './MovieFlatrateProviderCard';

import './ActorMovieCard.scss';

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

    const baseUrl = "https://image.tmdb.org/t/p/" + "w185" + "/";
    const defaultMovieUrl = "https://www.themoviedb.org/assets/2/v4/glyphicons/basic/glyphicons-basic-4-user-grey-d8fe957375e70239d6abdd549fd7568c89281b2179b5f4470e2e12895792dfa5.svg"
    const movieUrl = !movie.poster_path || movie.poster_path === "" || movie.poster_path === null 
        ? defaultMovieUrl 
        : baseUrl + movie.poster_path;

    let flatrateProviderLabel;
    if (flatrateProviders.length == 0) {
        flatrateProviderLabel = <span>Not currently on any streaming services</span>;
    } else {
        flatrateProviderLabel = <span>Available on these streaming services:</span>
    }

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