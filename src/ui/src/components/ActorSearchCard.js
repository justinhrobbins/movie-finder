import { React, useEffect, useState } from 'react';
import { ActorSearchResultsCard } from './ActorSearchResultsCard';

import './ActorSearchCard.scss';

export const ActorSearchCard = () => {
  const [showSearchResults, setShowSearchResults] = useState(false);
  const [actorName, setActorName] = useState("");
  const [actors, setActors] = useState([]);

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
    setActorName(searchBarQuery);
    
    if (searchBarQuery && searchBarQuery.length > 2) {
      fetchActors(searchBarQuery)
      setShowSearchResults(true);
    }
  };

  const clearSearchResults = () => {
    setShowSearchResults(false);
    setActorName("");
  };

  return (
    <div className="ActorSearchCard">
      <div className="actor-search-box">
        <label className="actor-search-label">Actor Search:</label>
        <input className="actor-search-box-input"
          id="actorSearchBoxInput"
          type="text"
          value={actorName}
          onChange={(e) => { handleSearchBarChange(e.target.value) }} />
      </div>

      {showSearchResults && (
        <div className="actor-search-results-container">
          {/* <div onClick={clearSearchResults} className="overlay"></div> */}
          <div className="actor-search-results-content" onClick={clearSearchResults}>
            {
              ((actorName && actorName.length > 0) && (!actors || actors.length === 0)) &&
              <h1>Actor not found</h1>
            }

            {
              (!actors || actors.length > 0) &&
              <h2 className="actor-search-page-results-label">Actor Search Results</h2>
            }

            {actors
              .slice(0, 5)
              .map(actor => <ActorSearchResultsCard key={actor.id} actor={actor} clearSearchResults={clearSearchResults} />)}
          </div>
        </div>
      )}
    </div>
  );
}