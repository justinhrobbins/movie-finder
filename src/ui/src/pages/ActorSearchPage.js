import { React, useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { ActorSearchCard } from '../components/ActorSearchCard';

export const ActorSearchPage = () => {
  const [actors, setActors] = useState([]);
  const { actorName } = useParams();

  useEffect(
    () => {
      const fetchActors = async () => {
        const response = await fetch(`http://localhost:8080/person/find?name=${actorName}`);
        const data = await response.json();
        setActors(data);
      };

      fetchActors();
    }, []
  );

  if (!actors || actors.length === 0) {
    return <h1>Actors not found</h1>
  }
  return (
    <div className="ActorSearchPage">
      <h2>Actors</h2>
      {actors.map(actor => <ActorSearchCard key={actor.id} actor={actor} />)}
    </div>
  );
}