import { React, useContext, useEffect, useState } from 'react';
import { UserContext } from "../UserContext";
import { MovieSubscriptionsCard } from './MovieSubscriptionsCard';

import './scss/MovieSubscriptionsListCard.scss';

export const MovieSubscriptionsListCard = ({ providedMovie, filterBySubscriptions }) => {
    const { loggedInUser } = useContext(UserContext);
    const [flatrateProviders, setFlatrateProviders] = useState(null);
    const [movie, setMovie] = useState(providedMovie);
    const [flatrateProviderLabel, setFlatrateProviderLabel] = useState(null);

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
        }, []
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
        <div className="MovieSubscriptionsListCard">
            <div className="actor-movie-card-providers-section-label">{flatrateProviderLabel}</div>
            <div className="actor-movie-card-providers-section-content">
                {
                    flatrateProviders.map(flatrateProvider => <MovieSubscriptionsCard key={movie.id} movie={movie} />)
                }
            </div>
        </div>
    );
}