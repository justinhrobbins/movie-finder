import { React, useEffect, useState } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { ActorSearchResultsCard } from '../components/ActorSearchResultsCard';

export const ActorSearchPage = () => {
  const [searchParams] = useSearchParams()
  const actorName = searchParams.get('actorName');
  const [actors, setActors] = useState([]);
  const navigate = useNavigate();

  useEffect(
    () => {
      fetchActors(actorName);
    }, []
  );

  const fetchActors = async (name) => {
    if (name && name.length > 0) {
      const response = await fetch(`http://localhost:8080/person/find?name=${name}`);
      const data = await response.json();
      setActors(data);
    }
  };

  const handleSearchBarChange = (searchBarQuery) => {
    if (searchBarQuery && searchBarQuery.length > 2) {
      navigate('?actorName=' + searchBarQuery);
      fetchActors(searchBarQuery)
    }
  };

  // if ((actorName && actorName.length > 0) && (!actors || actors.length === 0)) {
  //   return <h1>Actor not found</h1>
  // }

  return (
    <div className="ActorSearchPage">
      <label>Search</label>
      <input
        type="text"
        placeholder="Search..."
        onChange={(e) => { handleSearchBarChange(e.target.value) }} />

      {
        ((actorName && actorName.length > 0) && (!actors || actors.length === 0)) &&
        <h1>Actor not found</h1>
      }

      {
        (!actors || actors.length > 0) &&
        <h2>Actors</h2>
      }

      {actors.map(actor => <ActorSearchResultsCard key={actor.id} actor={actor} />)}
    </div>
  );
}