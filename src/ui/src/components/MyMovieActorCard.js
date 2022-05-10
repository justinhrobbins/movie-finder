import { React, useContext, useEffect, useState } from 'react';
import { useSearchParams, useLocation, Link } from 'react-router-dom';
import { UserContext } from "../UserContext";
import { ActorMovieCard } from './ActorMovieCard';
import { ActorAlertDetailCard } from './ActorAlertDetailCard';

import './scss/MyMovieActorCard.scss';

export const MyMovieActorCard = ({ actor }) => {
    const { loggedInUser } = useContext(UserContext);
    const actorDetailRoute = `/actors/${actor.actorId}`;
    const [searchParams, setSearchParams] = useSearchParams();
    const filterParam = searchParams.get('filter');

    const createActorLabel = () => {
        if (filterParam === "recent") {
            return <span>Recent releases</span>
        } else if (filterParam === "upcoming") {
            return <span>New releases</span>
        } else {
            return <span>Movies on your streaming subscriptions</span>
        }
    }

    if (!actor || !actor.movieCredits) {
        return <span>Searching for movies for: {actor.name}</span>
    }

    return (
        <div className="MyMovieActorCard">
            <div className="my-movie-actor-card-section">
                <div className="my-movie-actor-card-section-actor-details">
                    <div>
                        <ActorAlertDetailCard key={actor.actorId} providedActor={actor.person} actorDetails={actor.movieCounts} />
                    </div>
                </div>
                <div className="my-movie-actor-card-section-movies">
                    <div className="my-movie-actor-card-actor-name">
                        {createActorLabel()}
                    </div>
                    <div className="my-movie-actor-card-movies-container">
                        {
                            actor.movieCredits.cast
                                .map(
                                    movie =>
                                        <div className="my-movie-actor-card-movies-item">
                                            <ActorMovieCard
                                                key={movie.id}
                                                providedMovie={movie}
                                                shouldShowFullOverview={false}
                                                filterBySubscriptions={false} />
                                        </div>
                                )
                        }
                    </div>
                </div>
            </div>
        </div>
    );
}