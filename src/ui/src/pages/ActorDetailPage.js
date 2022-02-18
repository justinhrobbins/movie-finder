import { React, useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { ActorMovieCard } from '../components/ActorMovieCard';

export const ActorDetailPage = () => {
    const [person, setPerson] = useState({});
    const [personCredits, setPersonCredits] = useState({});
    const { actorId } = useParams();

    useEffect(
        () => {
            const fetchPersonCredits = async () => {
                const response = await fetch(`http://localhost:8080/person/${actorId}/movies`);
                const movieData = await response.json();
                setPersonCredits(movieData);
            };
            fetchPersonCredits();

            const fetchPerson = async () => {
                const response = await fetch(`http://localhost:8080/person/${actorId}`);
                const personData = await response.json();
                setPerson(personData);
            };
            fetchPerson();
        }, []
    );

    if (!person) {
        return <h1>Actor not found</h1>
    }

    if (!personCredits || !personCredits.cast) {
        return <h1>Movies not found for Actor</h1>
    }

    return (
        <div className="ActorDetailPage">
            <h2>Movies for {person.name}</h2>
            <hr />
            Most recent movie role: <ActorMovieCard key={personCredits.cast[0].id} movie={personCredits.cast[0]} />
            {personCredits.cast
                .slice(1)
                .map(movie => <ActorMovieCard key={movie.id} movie={movie} />)
            }
        </div>
    );
}