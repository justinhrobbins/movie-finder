import { React, useEffect, useState } from 'react';
import { ActorAlertDetailCard } from './ActorAlertDetailCard';

import './PopularActorsCard.scss';

export const PopularActorsCard = () => {
  const [popularActors, setPopularActors] = useState(null);

  useEffect(
    () => {
      const fetchPopularActors = async () => {
        try {
          const response = await fetch('http://localhost:8080/person/popular', {
            method: 'GET'
          });
          const popularActors = await response.json();
          setPopularActors(popularActors);
        } catch (error) {
          throw error;
        } finally {
        }
      };

      fetchPopularActors();
    }, []
  );

  if (!popularActors) {

    return <h3>Loading popular actors...</h3>
  }

  return (
    <div className="PopularActorsCard">
      <div className="popular-actors-card-header">
        <h2>Popular Actors:</h2>
      </div>
      <div className="popular-actors-card-list">
        {popularActors.actors
          .map(actor => <ActorAlertDetailCard key={actor.actorId} providedActor={actor.person} actorDetails={actor.details} />)
        }
      </div>
    </div>
  );
}