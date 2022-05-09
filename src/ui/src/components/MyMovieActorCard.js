import { React, useContext, useEffect, useState } from 'react';
import { useSearchParams, useLocation } from 'react-router-dom';
import { UserContext } from "../UserContext";
import { ActorMovieCard } from './ActorMovieCard';
import Select from 'react-select';

import './scss/MyMovieActorCard.scss';

export const MyMovieActorCard = ({ actor }) => {
    const { loggedInUser } = useContext(UserContext);

    if (!actor || !actor.movieCredits) {
        return <span>Searching for movies for: {actor.name}</span>
    }

    return (
        <div className="MyMovieActorCard">
            <div className="my-movie-actor-card-section">
                <h2 className="my-movie-actor-card-label">Movies for: {actor.person.name}</h2>
            </div>
            {
                actor.movieCredits.cast
                    .map(movie => <ActorMovieCard
                        key={movie.id}
                        providedMovie={movie}
                        filterBySubscriptions="false" />)
            }
        </div>
    );
}