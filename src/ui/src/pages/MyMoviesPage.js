import { React, useContext, useEffect, useState } from 'react';
import { useSearchParams, useLocation } from 'react-router-dom';
import { UserContext } from "../UserContext";
import { ActorAlertSummaryCard } from '../components/ActorAlertSummaryCard';
import { MovieCard } from '../components/MovieCard';
import Select from 'react-select';

import './scss/MyMoviesPage.scss';

export const MyMoviesPage = () => {
  const { loggedInUser } = useContext(UserContext);
  const [myMovies, setMyMovies] = useState();
  const location = useLocation();
  const [searchParams, setSearchParams] = useSearchParams();
  const filterParam = searchParams.get('filter') ? searchParams.get('filter') : '';

  useEffect(
    () => {
      const fetchMovies = async () => {
        try {
          const response = await fetch(process.env.REACT_APP_BACKEND_URL + `mymovies?filter=${filterParam}`, {
            method: 'GET',
            headers: {
              'Authorization': `Bearer ${loggedInUser.tokenId}`
            }
          });
          const myMovies = await response.json();
          setMyMovies(myMovies);
        } catch (error) {
          throw error;
        } finally {
        }
      };

      if (loggedInUser) {
        fetchMovies();
      }

      const filterOption = filterOptions.find(option => option.value === filterParam);
      if (filterOption) {
        sortAndFilterOptions.filterOption = filterOption;
      }
      setSortAndFilterOptions({ ...sortAndFilterOptions });
    }, [location, loggedInUser]
  );

  const createActorLabel = () => {
    if (filterParam === "recent") {
      return <span>Recent releases from your actors</span>
    } else if (filterParam === "upcoming") {
      return <span>Upcoming releases from your actors</span>
    } else {
      return <span>Movies from your actors on your streaming subscriptions</span>
    }
  }

  const [sortAndFilterOptions, setSortAndFilterOptions] = useState({
    "filterOption": {
      "value": 'all', "label": "All Movies"
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

  if (!myMovies || !myMovies.movies) {
    return <span>Searching for movies from your actors</span>
  }

  return (
    <div className="MyMoviesPage">
      <div className="my-movies-header-container">
        <div className="my-movies-header-dashboard">
          <ActorAlertSummaryCard actorCounts={myMovies.actorCounts} movieCounts={myMovies.movieCounts} />
        </div>
        <div className="my-movies-dropdown-filter">Flter by:
          <Select
            onChange={handleFilterChange}
            options={filterOptions}
            styles={colourStyles}
            value={sortAndFilterOptions.filterOption}
          />
        </div>
      </div>
      <div className="my-movies-label">
        <h2 >{createActorLabel()}</h2>
      </div>
      <div className="my-movie-movies-container">
        {
          myMovies.movies
            .map((movie) =>
              <div key={movie.credit.id} className="my-movie-movies-item">
                <MovieCard
                  key={movie.credit.id}
                  providedMovie={movie.credit}
                  shouldShowFullOverview={false} />
              </div>
            )
        }
      </div>
    </div>
  );
}