import { React, useContext, useEffect, useState } from 'react';
import { useSearchParams, useLocation } from 'react-router-dom';
import { UserContext } from "../UserContext";
import { MyMovieActorCard } from '../components/MyMovieActorCard';
import Select from 'react-select';

import './scss/MyMoviesPage.scss';

export const MyMoviesPage = () => {
  const { loggedInUser } = useContext(UserContext);
  const [movies, setMovies] = useState();
  const location = useLocation();
  const [searchParams, setSearchParams] = useSearchParams();
  const filterParam = searchParams.get('filter');

  useEffect(
    () => {
      const fetchMovies = async () => {
        try {
          const response = await fetch(process.env.REACT_APP_BACKEND_URL + `actoralerts/movies?filter=${filterParam}`, {
            method: 'GET',
            headers: {
              'Authorization': `Bearer ${loggedInUser.tokenId}`
            }
          });
          const movies = await response.json();
          setMovies((movies));
        } catch (error) {
          throw error;
        } finally {
        }
      };

      fetchMovies();

      const filterOption = filterOptions.find(option => option.value === filterParam);
      if (filterOption) {
        sortAndFilterOptions.filterOption = filterOption;
      }
      setSortAndFilterOptions({...sortAndFilterOptions});
    }, [location]
  );

  const createActorLabel = () => {
    if (filterParam === "recent") {
      return <span>Recent releases from you actors</span>
    } else if (filterParam === "upcoming") {
      return <span>Upcoming releases from your actors</span>
    } else {
      return <span>Movies from your actors on your streaming subscriptions</span>
    }
  }

  const [sortAndFilterOptions, setSortAndFilterOptions] = useState({
    "filterOption": {
      "value": 'all', "label": "All Movies"
    },
    "sortOption": {
      "value": "popularity", "label": "Popularity"
    }
  });
  const filterOptions = [
    { value: 'upcoming', label: 'Upcoming releases' },
    { value: 'recent', label: 'Recent release' },
    { value: 'subscriptions', label: 'Movies on my subscriptions' }
  ]

  const handleFilterChange = (selectedOption) => {
    let updatedSearchParams = new URLSearchParams(searchParams.toString());
    updatedSearchParams.set('filter', selectedOption.value);
    setSearchParams(updatedSearchParams.toString());
  };

  const colourStyles = {
    option: (base, state) => ({
      ...base,
      backgroundColor: state.isFocused ? "#808080" : "#A9A9A9",
      borderBottom: "1px solid black",
    }),
    control: (styles) => ({
      ...styles,
      backgroundColor: "#181a1e",
      minHeight: 36,
      width: "250px"
    }),
    singleValue: (provided, { data }) => ({
      ...provided,
      color: 'white',
    }),
  }

  if (!loggedInUser) {
    return <h3>Login to view Movies from your Actors</h3>
  }

  if (!movies || !movies.actors) {
    return <span>Searching for movies for your actors</span>
  }

  return (
    <div className="MyMoviesPage">
      <div className="my-movies-page-about-container">
        <div className="my-movies-page-about-intro">
          Movies from the actors you follow
          <br /><br />
          Use Actor Alerts to:
        </div>
        <ul className="my-movies-page-about-list">
          <li>Quickly find <b>upcoming movies</b> from your favorite actors</li>
        </ul>
      </div>
      <div className="my-movies-header-section">
        <h2 className="my-movies-header-section-label">{createActorLabel()}</h2>
        <div className="my-movies-header-section-filter">Flter by:
          <Select
            onChange={handleFilterChange}
            options={filterOptions}
            styles={colourStyles}
            value={sortAndFilterOptions.filterOption}
          />
        </div>
      </div>
      {
        movies.actors.actors
          .map(actor => <MyMovieActorCard key={actor.actorId} actor={actor} />)
      }
    </div>
  );
}