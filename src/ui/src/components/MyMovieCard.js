import { React } from 'react';
import { MovieCard } from './MovieCard';

import './scss/MyMovieCard.scss';

export const MyMovieCard = ({ movie }) => {

    return (
        <div className="MyMovieCard">
            <div className="my-movie-actor-card-section-movies">
                <div className="my-movie-actor-card-movies-container">
                    {
                        <div className="my-movie-actor-card-movies-item">
                            <MovieCard
                                key={movie.credit.id}
                                providedMovie={movie.credit}
                                shouldShowFullOverview={false}
                                filterBySubscriptions={false} />
                        </div>
                    }
                </div>
            </div>
        </div>
    );
}