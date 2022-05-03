import { React, useEffect, useState } from 'react';
import { ActorAlertDetailCard } from '../components/ActorAlertDetailCard';

import './PopularActorsPage.scss';

export const PopularActorsPage = () => {
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
    <div className="PopularActorsPage">
      <div className="actor-alerts-page-actors-list">
        {popularActors.actorAlerts
          .map(actorAlert => <ActorAlertDetailCard key={actorAlert.actorId} providedActor={actorAlert.person} actorDetails={actorAlert.details} />)
        }
      </div>
    </div>
  );
}