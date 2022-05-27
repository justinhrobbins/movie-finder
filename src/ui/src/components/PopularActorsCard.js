import { React, useEffect, useState } from 'react';
import { ActorDetailCard } from './ActorDetailCard';

import './scss/PopularActorsCard.scss';

export const PopularActorsCard = () => {
  const [popularActors, setPopularActors] = useState(null);

  useEffect(
    () => {
      const fetchPopularActors = async () => {
        try {
          const response = await fetch(process.env.REACT_APP_BACKEND_URL + `actors/popular`, {
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

  const removeActor = (actor) => {
  }

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
          .map(actor => 
            <div className="popular-actors-card-list-item" key={actor.actorId}>
              <ActorDetailCard key={actor.actorId} providedActor={actor} removeActor={removeActor} showActorBio={false} />
            </div>
            )
        }
      </div>
    </div>
  );
}