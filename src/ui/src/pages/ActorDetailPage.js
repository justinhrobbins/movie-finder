import { React, useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { ActorMovieCard } from '../components/ActorMovieCard';

export const ActorDetailPage = () => {
    const [personCredits, setPersonCredits] = useState({});
    const { actorId } = useParams();

    useEffect(
        () => {
            const fetchPersonCredits = async () => {
                const response = await fetch(`http://localhost:8080/person/${actorId}/movies`);
                const data = await response.json();
                setPersonCredits(data);
            };

            fetchPersonCredits();
        }, []
    );

    if (!personCredits || !personCredits.cast) {
        return <h1>Movies not found for Actor</h1>
    }

    return (
        <div className="ActorDetailPage">
            <h2>Movies</h2>
            {personCredits.cast
                .filter(credit => credit.media_type == "movie")
                .map(movie => <ActorMovieCard key={movie.id} movie={movie} />)
            }
        </div>
    );
}