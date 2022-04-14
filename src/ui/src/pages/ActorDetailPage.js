import { React, useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { ActorDetailCard } from '../components/ActorDetailCard';
import { ActorMovieCard } from '../components/ActorMovieCard';

import './ActorDetailPage.scss';

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
        return <h1>Searching for movies for {person.name}</h1>
    }

    return (
        <div className="ActorDetailPage">
            <ActorDetailCard key={person.id} actor={person}/>
            <h2 className="actor-detail-card-label">Movies for {person.name}:</h2>
            {personCredits.cast
                .map(movie => <ActorMovieCard key={movie.id} movie={movie} />)
            }
        </div>
    );
}