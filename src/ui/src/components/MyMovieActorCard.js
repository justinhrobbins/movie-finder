import { React, useContext } from 'react';
import { useSearchParams } from 'react-router-dom';
import { ActorMovieCard } from './ActorMovieCard';
import { ActorAlertDetailCard } from './ActorAlertDetailCard';

import './scss/MyMovieActorCard.scss';

export const MyMovieActorCard = ({ actor }) => {
    const [searchParams] = useSearchParams();
    const filterParam = searchParams.get('filter');

    if (!actor || !actor.movieCredits) {
        return <span>Searching for movies for: {actor.name}</span>
    }

    return (
        <div className="MyMovieActorCard">
            <div className="my-movie-actor-card-section">
                <div className="my-movie-actor-card-section-actor-details">
                    <div>
                        <ActorAlertDetailCard key={actor.actorId} providedActor={actor} />
                    </div>
                </div>
                <div className="my-movie-actor-card-section-movies">

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