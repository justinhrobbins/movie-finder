import { React, useEffect, useState } from 'react';
import { ActorDetails } from './ActorDetails';

import './scss/PopularActors.scss';

export const PopularActors = () => {
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
    <div className="PopularActors">
      <div className="popular-actors-header">
        <h2>Popular Actors:</h2>
      </div>
      <div className="popular-actors-list">
        {popularActors.actors
          .map(actor => 
            <div className="popular-actors-list-item" key={actor.actorId}>
              <ActorDetails key={actor.actorId} providedActor={actor} removeActor={removeActor} showActorBio={false} />
            </div>
            )
        }
      </div>
    </div>
  );
}