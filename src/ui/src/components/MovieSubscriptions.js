import { React, useContext, useEffect, useState } from 'react';
import { UserContext } from "../UserContext";

import './scss/MovieSubscriptions.scss';

export const MovieSubscriptions = ({ providedMovie }) => {
    const { loggedInUser } = useContext(UserContext);
    const [flatrateProviders, setFlatrateProviders] = useState(null);
    const [movie, setMovie] = useState(providedMovie);
    const [flatrateProviderLabel, setFlatrateProviderLabel] = useState(null);

    useEffect(
        () => {
            setMovie(providedMovie);
        }, [providedMovie]
    );

    useEffect(
        () => {
            let isMounted = true;
            const fetchWatchProviders = async () => {
                const response = await fetch(process.env.REACT_APP_BACKEND_URL + `movies/${movie.id}/watchproviders`);
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
        }, [movie]
    );

    useEffect(
        () => {
            if (!flatrateProviders || flatrateProviders.length == 0) {
                setFlatrateProviderLabel(<span>Not currently on any streaming services</span>);
            } else {
                setFlatrateProviderLabel(<span>Available on these streaming services:</span>);
            }
        }, [flatrateProviders]
    );

    const flatrateProviderUrl = (flatrateProvider) => {
        const baseUrl = "https://image.tmdb.org/t/p/w45/";
        const defaultMovieUrl = "https://www.themoviedb.org/assets/2/v4/glyphicons/basic/glyphicons-basic-4-user-grey-d8fe957375e70239d6abdd549fd7568c89281b2179b5f4470e2e12895792dfa5.svg"
        return !flatrateProvider.logo_path || flatrateProvider.logo_path === ""
            ? defaultMovieUrl
            : baseUrl + flatrateProvider.logo_path;
    };

    if (!flatrateProviders) return null;

    return (
        <div className="MovieSubscriptions">
            <div className="movie-subscription-provider-label">{flatrateProviderLabel}</div>
            <div className="movie-subscription-provider-container">
                {
                    flatrateProviders.map(flatrateProvider =>
                        <div key={flatrateProvider.provider_id} className="movie-subscriptions">
                            <div className="movie-subscription-image"><img alt={flatrateProvider.provider_name} title={flatrateProvider.provider_name} src={flatrateProviderUrl(flatrateProvider)} /></div>
                        </div>
                    )
                }
            </div>
        </div>
    );
}